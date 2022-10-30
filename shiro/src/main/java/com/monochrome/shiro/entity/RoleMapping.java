package com.monochrome.shiro.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author monochrome
 * @date 2022/10/28
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Entity(name = "role_mapping")
public class RoleMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    Integer uid;
    Integer rid;

}
