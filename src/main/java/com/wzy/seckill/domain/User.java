package com.wzy.seckill.domain;

import lombok.Data;

/**
 * 测试用户表
 * @author wzy
 * @version 1.0
 * @date 2019/12/16 10:52
 */
@Data
public class User {
    /**
     * id
     */
    private Integer id;
    /**
     * 用户名
     */
    private String name;
}
