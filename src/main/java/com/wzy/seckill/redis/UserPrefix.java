package com.wzy.seckill.redis;

/**
 * 用户前缀
 * @author wzy
 * @version 1.0
 * @date 2019/12/17 10:09
 */
public class UserPrefix extends BasePrefix {
    private UserPrefix(String prefix) {
        super(prefix);
    }

    private UserPrefix(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static UserPrefix getById = new UserPrefix("id");
    public static UserPrefix getByName = new UserPrefix("name");
}
