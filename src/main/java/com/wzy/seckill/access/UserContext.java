package com.wzy.seckill.access;

import com.wzy.seckill.domain.SeckillUser;

public class UserContext {
    public static ThreadLocal<SeckillUser> userThreadLocal = new ThreadLocal<>();

    public static void setUser(SeckillUser user) {
        userThreadLocal.set(user);
    }

    public static SeckillUser getUser() {
       return userThreadLocal.get();
    }
}
