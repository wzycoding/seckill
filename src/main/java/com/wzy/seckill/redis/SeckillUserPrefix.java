package com.wzy.seckill.redis;

/**
 * 用户前缀
 * @author wzy
 * @version 1.0
 * @date 2019/12/17 10:09
 */
public class SeckillUserPrefix extends BasePrefix {
    public static final int TOKEN_EXPIRE = 3600 * 24 * 2;
    private SeckillUserPrefix(String prefix) {
        super(prefix);
    }

    private SeckillUserPrefix(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static SeckillUserPrefix token = new SeckillUserPrefix(TOKEN_EXPIRE, "tk");
    public static SeckillUserPrefix getById = new SeckillUserPrefix(0, "id");

}
