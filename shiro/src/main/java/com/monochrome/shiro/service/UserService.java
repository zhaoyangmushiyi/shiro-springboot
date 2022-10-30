package com.monochrome.shiro.service;

import com.monochrome.shiro.entity.User;

/**
 * @author monochrome
 * @date 2022/10/7
 */
public interface UserService {
    User getUserByName(String name);
}
