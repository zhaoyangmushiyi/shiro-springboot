package com.monochrome.shiro.config;

import com.monochrome.shiro.realm.CustomRealm;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.mgt.RememberMeManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

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

//    //配置 SecurityManager
//    @Bean
//    public DefaultWebSecurityManager defaultWebSecurityManager() {
//        //1 创建 defaultWebSecurityManager 对象
//        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
//        //2 创建加密对象，并设置相关属性
//        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
//        //2.1 采用 md5 加密
//        matcher.setHashAlgorithmName("md5");
//        //2.2 迭代加密次数
//        matcher.setHashIterations(3);
//        //3 将加密对象存储到 myRealm 中
//        realm.setCredentialsMatcher(matcher);
//        //4 将 myRealm 存入 defaultWebSecurityManager 对象
//        defaultWebSecurityManager.setRealm(realm);
//        //5 返回
//        return defaultWebSecurityManager;
//    }
    //配置 SecurityManager，实现多个Realm
    @Bean
    public DefaultWebSecurityManager defaultWebSecurityManager() {
        //1 创建 defaultWebSecurityManager 对象
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        //2 创建认证对象，并设置认证策略
        ModularRealmAuthenticator modularRealmAuthenticator = new ModularRealmAuthenticator();
        //有多种认证策略，AtLeastOneSuccessfulStrategy，FirstSuccessfulStrategy，AllSuccessfulStrategy
        modularRealmAuthenticator.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy());
        defaultWebSecurityManager.setAuthenticator(modularRealmAuthenticator);
        //3 封装Realm集合
        List<Realm> realms = new ArrayList<>();
        //3.1 创建加密对象，并设置相关属性
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
        //3.1.1 采用 md5 加密
        matcher.setHashAlgorithmName("md5");
        //3.1.2 迭代加密次数
        matcher.setHashIterations(3);
        //3.1.2 将加密对象存储到 myRealm 中
        realm.setCredentialsMatcher(matcher);
        realms.add(realm);
        //4 将 realms 集合存入 defaultWebSecurityManager 对象
        defaultWebSecurityManager.setRealms(realms);
        //5 设置 remember me
        defaultWebSecurityManager.setRememberMeManager(createRememberMeManager());
        //6 返回
        return defaultWebSecurityManager;
    }

    /**
     * 创建rememberMeManager
     * @return rememberMeManager
     */
    private RememberMeManager createRememberMeManager() {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(createRememberMeCookie());
        cookieRememberMeManager.setCipherKey("1234567890987654".getBytes());
        return cookieRememberMeManager;
    }

    /**
     * 创建Cookie
     * @return cookie
     */
    private Cookie createRememberMeCookie() {
        SimpleCookie cookie = new SimpleCookie("rememberMe");
        //设置跨域
        //cookie.setDomain(domain);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(30 * 24 * 60 * 60);
        return cookie;
    }

    //配置 Shiro 内置过滤器拦截范围
    @Bean
    public DefaultShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition definition = new DefaultShiroFilterChainDefinition();
        //设置不认证可以访问的资源
        definition.addPathDefinition("/user/login", "anon");
        definition.addPathDefinition("/user/userLogin", "anon");
        //配置登出过滤器
        definition.addPathDefinition("/logout", "logout");
        //设置需要进行登录认证的拦截范围
        definition.addPathDefinition("/**", "authc");
        //添加存在用户的过滤器(rememberMe)
        definition.addPathDefinition("/**", "user");
        return definition;
    }

    /**
     * 开启Shiro注解(如@RequiresRoles,@RequiresPermissions),
     * 需借助SpringAOP扫描使用Shiro注解的类,并在必要时进行安全逻辑验证
     * 配置以下两个bean(DefaultAdvisorAutoProxyCreator和AuthorizationAttributeSourceAdvisor)
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        /*
         * setUsePrefix(false)用于解决一个奇怪的bug。在引入spring aop的情况下。
         * 在@Controller注解的类的方法中加入@RequiresRole注解，会导致该方法无法映射请求，导致返回404。
         * 加入这项配置能解决这个bug
         */
        advisorAutoProxyCreator.setUsePrefix(true);
        // advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }

}
