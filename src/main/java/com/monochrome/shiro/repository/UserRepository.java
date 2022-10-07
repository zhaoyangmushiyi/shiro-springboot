package com.monochrome.shiro.repository;

import com.monochrome.shiro.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author monochrome
 * @date 2022/10/7
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findUserByName(String name);
}
