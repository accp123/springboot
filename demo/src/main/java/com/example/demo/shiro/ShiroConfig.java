package com.example.demo.shiro;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/*
    配置类
 */
@Configuration
public class ShiroConfig {
    //创建ShiroFilterFactoryBean
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("securityManager") DefaultWebSecurityManager securityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        //设置安全管理器
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        //添加Shiro内置过滤器
        Map<String,String> filterMap = new LinkedHashMap<String,String>();
        /*filterMap.put("/adds","authc");
        filterMap.put("/updates","authc");*/

        filterMap.put("/test","anon");
        filterMap.put("/login","anon");

        //授权过滤器
        //拦截后会自动跳转到未授权页面
        filterMap.put("/adds","perms[user:add]");

        filterMap.put("/**","anon");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);
        //修改调整登录页面
        shiroFilterFactoryBean.setLoginUrl("/toLogin");

        //设置未授权提示页面
        shiroFilterFactoryBean.setUnauthorizedUrl("/noAuth");

        return  shiroFilterFactoryBean;
    }
    //创建DefaultWebSecurityManager
    @Bean(name="securityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("userRealm") UserRealm userRealm){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //关联realm
        securityManager.setRealm(userRealm);
        return securityManager;
    }
    //创建Realm
    @Bean(name = "userRealm")
    public UserRealm getRealm()
    {
        return new UserRealm();
    }

    //配置ShiroDialect,用于thymeleaf和shiro标签配合使用
   @Bean
    public ShiroDialect getShiroDialect(){
        return new ShiroDialect();
    }
}
