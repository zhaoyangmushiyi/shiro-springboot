package com.monochrome.shiro.repository;

import com.monochrome.shiro.entity.RoleMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author monochrome
 * @date 2022/10/28
 */
@Repository
public interface RoleMappingRepository extends JpaRepository<RoleMapping, Integer> {

    List<RoleMapping> findRoleMappingsByUid(Integer uid);

}
