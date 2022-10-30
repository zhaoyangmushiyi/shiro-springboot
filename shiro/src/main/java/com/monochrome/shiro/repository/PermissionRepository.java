package com.monochrome.shiro.repository;

import com.monochrome.shiro.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author monochrome
 * @date 2022/10/30
 */
public interface PermissionRepository extends JpaRepository<Permission, Integer> {

    @Query("select p from #{#entityName} p, role_permission_mapping rp where p.id = rp.pid and rp.rid in ?1")
    List<Permission> findPermissionsByRids(List<Integer> rids);
}
