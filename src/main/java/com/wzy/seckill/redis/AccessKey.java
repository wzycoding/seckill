package com.wzy.seckill.redis;

public class AccessKey extends BasePrefix {
    public AccessKey(int expire, String prefix) {
        super(expire, prefix);
    }

    public static AccessKey withExpire(int expire) {
        return new AccessKey(expire, "access");
    }

}
