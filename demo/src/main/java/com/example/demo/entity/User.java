package com.example.demo.entity;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.io.*;
import java.util.Date;

public class User implements  Serializable {
    private Integer id;
    @Size(min = 4, max = 15, message = "用户名长度为4-15位")
    private String userName;
   // @Pattern(regexp = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$", message = "手机号填写错误")

    private  String password;
   // @Pattern(regexp = "^[0-9]{4}-[0-9]{2}-[0-9]{2}$",message = "日期格式不对")
   // @DateTimeFormat(pattern="yyyy-MM-dd hh:mm:ss")
    @NotNull(message = "生日不能为空")
    private Date birthday;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }
}
