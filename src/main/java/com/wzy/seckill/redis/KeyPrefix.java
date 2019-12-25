package com.wzy.seckill.redis;

/**
 * 前缀Prefix
 * @author wzy
 * @version 1.0
 * @date 2019/12/16 22:48
 */
public interface KeyPrefix {
    public int expireSeconds();

    public String getPrefix();

}
