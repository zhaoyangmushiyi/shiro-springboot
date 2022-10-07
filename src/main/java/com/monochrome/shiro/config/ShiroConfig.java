package com.monochrome.shiro.config;

import com.monochrome.shiro.realm.CustomRealm;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author monochrome
 * @date 2022/10/7
 */
@Configuration
public class ShiroConfig {

    private final CustomRealm realm;

    public ShiroConfig(CustomRealm realm) {
        this.realm = realm;
    }

    //配置 SecurityManager
    @Bean
    public DefaultWebSecurityManager defaultWebSecurityManager() {
        //1 创建 defaultWebSecurityManager 对象
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        //2 创建加密对象，并设置相关属性
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
        //2.1 采用 md5 加密
        matcher.setHashAlgorithmName("md5");
        //2.2 迭代加密次数
        matcher.setHashIterations(3);
        //3 将加密对象存储到 myRealm 中
        realm.setCredentialsMatcher(matcher);
        //4 将 myRealm 存入 defaultWebSecurityManager 对象
        defaultWebSecurityManager.setRealm(realm);
        //5 返回
        return defaultWebSecurityManager;
    }

    //配置 Shiro 内置过滤器拦截范围
    @Bean
    public DefaultShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition definition = new DefaultShiroFilterChainDefinition();
        //设置不认证可以访问的资源
        definition.addPathDefinition("/user/login", "anon");
        definition.addPathDefinition("/user/userLogin", "anon");
        //设置需要进行登录认证的拦截范围
        definition.addPathDefinition("/**", "authc");
        return definition;
    }

}
