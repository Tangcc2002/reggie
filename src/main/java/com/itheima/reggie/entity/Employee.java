package com.itheima.reggie.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 员工实体
 */
@Data
public class Employee implements Serializable {

    /**
     * 序列化ID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 员工ID
     */
    private Long id;

    /**
     * 员工账号
     */
    private String username;

    /**
     * 员工姓名
     */
    private String name;

    /**
     * 密码
     */
    private String password;

    /**
     * 员工手机号
     */
    private String phone;

    /**
     * 员工性别
     */
    private String sex;

    /**
     * 员工身份证
     */
    private String idNumber;

    /**
     * 账号是否启用
     */
    private Integer status;

    /**
     * 员工账号创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 员工账号修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 创建员工账号的用户的ID
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 修改员工账号的用户的ID
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

}
