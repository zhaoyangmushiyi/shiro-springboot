CREATE DATABASE IF NOT EXISTS `shirodb` CHARACTER SET utf8mb4;
USE `shirodb`;
CREATE TABLE `user`
(
    `id`   BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
    `name` VARCHAR(30) DEFAULT NULL COMMENT '用户名',
    `pwd`  VARCHAR(50) DEFAULT NULL COMMENT '密码',
    `rid`  BIGINT(20)  DEFAULT NULL COMMENT '角色编号',
    PRIMARY KEY (`id`)
) ENGINE = INNODB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8 COMMENT ='用户表';
INSERT INTO shirodb.user (name, pwd, rid) VALUES ('zhangsan', '7174f64b13022acd3c56e2781e098a5f', null);
INSERT INTO shirodb.user (name, pwd, rid) VALUES ('lisi', '7174f64b13022acd3c56e2781e098a5f', null);

create user shiro
    identified by 'shiro';

grant all on shirodb.* to shiro;


CREATE TABLE `role`
(
    `id`       BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
    `name`     VARCHAR(30) DEFAULT NULL COMMENT '角色名',
    `desc`     VARCHAR(50) DEFAULT NULL COMMENT '描述',
    `role_name` VARCHAR(20) DEFAULT NULL COMMENT '角色显示名',
    PRIMARY KEY (`id`)
) ENGINE = INNODB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8 COMMENT ='角色表';

INSERT INTO shirodb.role (name, `desc`, role_name) VALUES ('admin', '所有权限', '管理员');
INSERT INTO shirodb.role (name, `desc`, role_name) VALUES ('user_manager', '用户管理员权限', '用户管理员');

CREATE TABLE `role_user_mapping`
(
    `id`  BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
    `uid` BIGINT(20) DEFAULT NULL COMMENT '用户id',
    `rid` BIGINT(20) DEFAULT NULL COMMENT '角色id',
    PRIMARY KEY (`id`)
) ENGINE = INNODB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8 COMMENT ='角色用户映射表';

INSERT INTO shirodb.role_user_mapping (uid, rid)VALUES (1, 1);
INSERT INTO shirodb.role_user_mapping (uid, rid)VALUES (1, 2);
INSERT INTO shirodb.role_user_mapping (uid, rid)VALUES (2, 2);

CREATE TABLE permission
(
    `id`   BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
    `name` VARCHAR(30) DEFAULT NULL COMMENT '权限名',
    `info` VARCHAR(30) DEFAULT NULL COMMENT '权限信息',
    `desc` VARCHAR(50) DEFAULT NULL COMMENT '描述',
    PRIMARY KEY (`id`)
) ENGINE = INNODB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8 COMMENT ='权限表';
INSERT INTO shirodb.permission (name, info, `desc`) VALUES ('删除用户', 'user:delete', '删除用户');
INSERT INTO shirodb.permission (name, info, `desc`) VALUES ('新增用户', 'user:add', '新增用户');
INSERT INTO shirodb.permission (name, info, `desc`) VALUES ('修改用户', 'user:edit', '修改用户');

CREATE TABLE `role_permission_mapping`
(
    `id`  BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
    `rid` BIGINT(20) DEFAULT NULL COMMENT '角色id',
    `pid` BIGINT(20) DEFAULT NULL COMMENT '权限id',
    PRIMARY KEY (`id`)
) ENGINE = INNODB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8 COMMENT ='角色权限映射表';
INSERT INTO shirodb.role_permission_mapping (rid, pid) VALUES (1, 1);
INSERT INTO shirodb.role_permission_mapping (rid, pid) VALUES (1, 2);
INSERT INTO shirodb.role_permission_mapping (rid, pid) VALUES (1, 3);
