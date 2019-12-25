package com.wzy.seckill.redis;


/**
 * KeyPrefix实现类,抽象类
 * @author wzy
 * @version 1.0
 * @date 2019/12/16 22:51
 */
public abstract class BasePrefix implements KeyPrefix {

    /**
     * 过期时间
     */
    private int expireSeconds;

    /**
     * 前缀
     */
    private String prefix;

    @Override
    public int expireSeconds() {
        return expireSeconds;
    }

    @Override
    public String getPrefix() {
        String className = getClass().getSimpleName();
        return className + ":" + prefix;
    }

    public BasePrefix(String prefix) {
        this.prefix = prefix;
    }

    public BasePrefix(int expireSeconds, String prefix) {
        this.expireSeconds = expireSeconds;
        this.prefix = prefix;
    }


}
