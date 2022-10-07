package com.monochrome.shiro.realm;

import com.monochrome.shiro.entity.User;
import com.monochrome.shiro.service.UserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.stereotype.Component;

/**
 * @author monochrome
 * @date 2022/10/7
 */
@Component
public class CustomRealm extends AuthorizingRealm {

    private final UserService userService;

    public CustomRealm(UserService userService) {
        this.userService = userService;
    }

    //自定义授权方法
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }

    //自定义登录认证方法
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        // 1 获取用户身份信息
        String name = token.getPrincipal().toString();
        // 2 调用业务层获取用户信息(数据库中)
        User user = userService.getUserByName(name);
        if (user != null) {
            SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(
                    token.getPrincipal(),
                    user.getPwd(),
                    ByteSource.Util.bytes("salt"),
                    name
            );
            return info;
        }
        return null;
    }
}
