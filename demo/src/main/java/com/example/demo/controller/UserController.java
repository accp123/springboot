package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.*;
import java.util.List;

@Controller
@Api("swaggerTestController相关api")
public class UserController {

    @Resource
    private UserService userService;
    //---------------------shiro开始-------------
    @RequestMapping(value = "/test")
    public String test()
    {
        return "test";
    }
    @RequestMapping(value="/adds")
    public String add()
    {
        return "user/add";
    }
    @RequestMapping(value="/updates")
    public String update()
    {
        return "user/update";
    }
    @RequestMapping(value="/toLogin")
    public String toLogin()
    {
        return "login";
    }
    @RequestMapping(value="/login")
    public String login(String name, String password, Model model, HttpSession session)
    {
        /**
         * 使用Shiro编写认证操作
         */
        //1.获取Subject对象
        Subject subject = SecurityUtils.getSubject();
       //2.封装用户数据
        UsernamePasswordToken token = new UsernamePasswordToken(name,password);
        //3.执行登录方法
        try {
            subject.login(token);//无异常代表登录成功
            User user=(User) subject.getPrincipal();

            System.out.println(user.getUserName()+"###########");

            session.setAttribute("user", user);
            return "redirect:/test";
        } catch (UnknownAccountException e) {
            //登录失败：账户名不存在
            model.addAttribute("msg","用户名不存在");
            return "login";
        } catch (IncorrectCredentialsException e) {
            //登录失败：密码错误
            model.addAttribute("msg","密码错误");
            return "login";
        }

    }

    //未授权提示
    @RequestMapping(value = "/noAuth")
    public  String noAuth()
    {
        return "noAuth";
    }
//---------------------shiro结束-------------
    @RequestMapping(value = "/chat")
    public  String chat(@ModelAttribute User user)
    {

        return "chat";
    }


    @ApiIgnore
    @RequestMapping(value = "/addUI")
    public  String addUI(@ModelAttribute User user)
    {

        return "add";
    }

    @ApiIgnore
    @RequestMapping(value = "/add")
    public  String add(@Validated User user,BindingResult bindingResult,Model model )
    { model.addAttribute("user",user);
        if(bindingResult.hasErrors()){

                return "add";
         }
        System.out.println(user.getId());
        System.out.println(user.getUserName());
        this.userService.insert(user);
        return "redirect:/getList";
    }


    /**
     * 根据ID查询用户
     * @param id
     * @return
     */
    @ApiOperation(value="获取用户详细信息", notes="根据url的id来获取用户详细信息")
    @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "Integer", paramType = "path")
    @RequestMapping(value = "findById/{id}", method = RequestMethod.GET)
    public @ResponseBody   User findById(@PathVariable(value = "id")  Integer id)
    {
        User user = this.userService.getById(1);

        return user;
    }





    @ApiIgnore
    @RequestMapping(value = "/getList")
    public String getList(Model model,@RequestParam(defaultValue = "1") Integer pageNum)
    {
        PageHelper.startPage(pageNum,5);
         List<User> userList = this.userService.getList();
        PageInfo<User> pageInfo = new PageInfo<User>(userList);
        model.addAttribute("pageInfo",pageInfo);

        return "list";
    }


    @RequestMapping(value = "/getListAjax")
    public @ResponseBody List<User> getListAjax(Model model)
    {
        List<User> userList = this.userService.getList();
        return userList;
    }






    @RequestMapping(value = "/delete")
    public  String delete(Integer id)
    {

        this.userService.deleteById(id);
        return "redirect:/getList";
    }
    @RequestMapping(value = "/updateUI")
    public  String updateUI(Integer id,Model model)
    {
        User user = this.userService.getById(id);
        model.addAttribute("user",user);
        return "update";
    }

    @RequestMapping(value = "/updateUIA/{id}")
    public  String updateUIA(@PathVariable Integer id, Model model)
    {
        User user = this.userService.getById(id);
        model.addAttribute("user",user);
        return "update";
    }

    @RequestMapping(value = "/update")
    public  String update(User user)
    {
       this.userService.edit(user);
        return "redirect:/getList";
    }

//文件上传页面
    @RequestMapping(value = "/file")
    public String file()
    {
        return "file";
    }

    //处理文件上传
    @RequestMapping(value="/testuploadimg", method = RequestMethod.POST)
    @ResponseBody
    public String upload(MultipartFile file) throws Exception {
        if(file.isEmpty()){
            return "false";
        }
        String fileName = file.getOriginalFilename();
        int size = (int) file.getSize();
        System.out.println(fileName + "-->" + size);

        String path = "G:/testFile" ;
        File dest = new File(path + "/" + fileName);
        if(!dest.getParentFile().exists()){ //判断文件父目录是否存在
            dest.getParentFile().mkdir();
        }
        try {
            file.transferTo(dest); //保存文件
            return "true";
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "false";
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "false";
        }
    }

    @RequestMapping("download")
    public String downLoad(HttpServletResponse response){
        String filename="ACCP1.jpg";
        String filePath = "G:/testFile" ;
        File file = new File(filePath + "/" + filename);
        if(file.exists()){ //判断文件父目录是否存在
            response.setContentType("application/force-download");
            response.setHeader("Content-Disposition", "attachment;fileName=" + filename);

            byte[] buffer = new byte[1024];
            FileInputStream fis = null; //文件输入流
            BufferedInputStream bis = null;

            OutputStream os = null; //输出流
            try {
                os = response.getOutputStream();
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                int i = bis.read(buffer);
                while(i != -1){
                    os.write(buffer);
                    i = bis.read(buffer);
                }

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            System.out.println("----------file download" + filename);
            try {
                bis.close();
                fis.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return null;
    }
   /* @InitBinder
    public void InitBinder(WebDataBinder dataBinder)
    {
        dataBinder.registerCustomEditor(Date.class, new PropertyEditorSupport()
            {
                public void setAsText(String value) {
                    try { setValue(new SimpleDateFormat("yyyy-MM-dd").parse(value));
                    } catch(ParseException e) { setValue(null); }
                }
                public String getAsText()
                {
                    return new SimpleDateFormat("yyyy-MM-dd").format((Date) getValue());
                }
            }
        );
    }*/

}
