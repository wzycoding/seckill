package com.wzy.seckill.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


/**
 * 秒杀用户实体类
 * @author wzy
 * @version 1.0
 * @date 2019/12/19 10:50
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeckillUser {
    /**
     * 用户id
     */
    private Long id;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 密码
     */
    private String password;

    /**
     * 第二次md5加密盐值
     */
    private String salt;

    /**
     * 头像地址
     */
    private String head;

    /**
     * 注册时间
     */
    private Date registerDate;

    /**
     * 上次登录时间
     */
    private Date LastLoginDate;

    /**
     * 登录次数
     */
    private Integer loginCount;
}
