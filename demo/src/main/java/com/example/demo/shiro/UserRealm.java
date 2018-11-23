package com.example.demo.shiro;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import javax.annotation.Resource;

/**
 * 自定义Realm类
 */
public class UserRealm extends AuthorizingRealm {

    @Resource
    private UserService userService;
    /**
     * 执行授权逻辑
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
       User user = (User)principalCollection.getPrimaryPrincipal();
        System.out.println(user.getId()+"%%%%%"+user.getUserName());
      System.out.println(" 执行授权逻辑");
      //给资源进行授权
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        //添加资源授权字符串  【实际中查数据库】
        info.addStringPermission("user:add");
        return info;
    }
    /**
     * 执行认证逻辑
     * @return
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println(" 执行认证逻辑");
        //假如数据库的用户名和密码分别为 accp   123456
        //编写Shiro判断逻辑，用户名和密码的判断
        //1.判断用户名
        UsernamePasswordToken token = (UsernamePasswordToken)authenticationToken;

        User user = this.userService.getByName(token.getUsername());
        if(user==null)
        {
            //用户名不存在 Shiro底层会抛出UnknownAccountException异常
            return null;
        }
        //判断密码
        return new SimpleAuthenticationInfo(user,user.getPassword(),"");
    }
}
