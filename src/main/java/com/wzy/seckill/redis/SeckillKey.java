package com.wzy.seckill.redis;

public class SeckillKey extends BasePrefix{
    public SeckillKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }
    public static SeckillKey getSeckillVerifyCode = new SeckillKey(0, "verifyCode");
    public static SeckillKey getSeckillPath = new SeckillKey(0, "path");
}
