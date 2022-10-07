package com.monochrome.shiro.service.impl;

import com.monochrome.shiro.entity.User;
import com.monochrome.shiro.repository.UserRepository;
import com.monochrome.shiro.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @author monochrome
 * @date 2022/10/7
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getUserByName(String name) {
        return userRepository.findUserByName(name);
    }
}
