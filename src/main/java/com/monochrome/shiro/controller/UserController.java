package com.monochrome.shiro.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @author monochrome
 * @date 2022/10/7
 */
@Controller
@RequestMapping("user")
public class UserController {

    @GetMapping("userLogin")
    public String userLogin(String name, String pwd, @RequestParam(defaultValue = "false") boolean rememberMe, HttpSession session) {
        try {
            //1 获取 Subject 对象
            Subject subject = SecurityUtils.getSubject();
            //2 封装请求数据到 token 对象中
            AuthenticationToken token = new UsernamePasswordToken(name, pwd, rememberMe);
            //3 调用 login 方法进行登录认证
            subject.login(token);
            session.setAttribute("user", token.getPrincipal().toString());
            return "main";
        } catch (AuthenticationException e) {
            e.printStackTrace();
            return "登录失败";
        }
    }

    //登录认证验证 rememberMe
    @GetMapping("userLoginRm")
    public String userLogin(HttpSession session) {
        session.setAttribute("user","rememberMe");
        return "main";
    }

    //跳转登录页面
    @GetMapping("login")
    public String login() {
        return "login";
    }
}
