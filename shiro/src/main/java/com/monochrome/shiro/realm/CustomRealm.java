package com.monochrome.shiro.realm;

import com.monochrome.shiro.entity.Permission;
import com.monochrome.shiro.entity.Role;
import com.monochrome.shiro.entity.RoleUserMapping;
import com.monochrome.shiro.entity.User;
import com.monochrome.shiro.repository.PermissionRepository;
import com.monochrome.shiro.repository.RoleUserMappingRepository;
import com.monochrome.shiro.repository.RoleRepository;
import com.monochrome.shiro.service.UserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author monochrome
 * @date 2022/10/7
 */
@Component
public class CustomRealm extends AuthorizingRealm {

    private Logger logger = LoggerFactory.getLogger(CustomRealm.class);

    private final UserService userService;
    private final RoleRepository roleRepository;
    private final RoleUserMappingRepository roleUserMappingRepository;
    private final PermissionRepository permissionRepository;

    public CustomRealm(UserService userService, RoleRepository roleRepository, RoleUserMappingRepository roleUserMappingRepository, PermissionRepository permissionRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.roleUserMappingRepository = roleUserMappingRepository;
        this.permissionRepository = permissionRepository;
    }

    //自定义授权方法
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        logger.info("进入自定义授权方法");
        String username = principals.getPrimaryPrincipal().toString();
        User user = userService.getUserByName(username);
        List<RoleUserMapping> roleUserMappings = roleUserMappingRepository.findRoleUserMappingsByUid(user.getId());
        List<Integer> roleIds = roleUserMappings.stream()
                .map(RoleUserMapping::getRid)
                .collect(Collectors.toList());
        List<Role> roles = roleRepository.findRolesByIdIn(roleIds);
        List<String> roleNames = roles.stream()
                .map(Role::getName)
                .collect(Collectors.toList());
        logger.info("current user's roles:{}", roleNames);
        List<Permission> permissions = permissionRepository.findPermissionsByRids(roleIds);
        List<String> permissionInfos = permissions.stream()
                .map(Permission::getInfo)
                .collect(Collectors.toList());
        logger.info("current user's permissions:{}", permissionInfos);
        //1 创建对象，存储当前登录的用户的权限和角色
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        //2 存储角色
        info.addRoles(roleNames);
        //3 存储权限
        info.addStringPermissions(permissionInfos);
        //返回
        return info;
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
