package com.wzy.seckill.util;

import java.util.UUID;

/**
 * @author wzy
 * @version 1.0
 * @date 2019/12/19 14:09
 */
public class UUIDUtil {
    public static String uuid() {
        return UUID.randomUUID().toString().replace("_", "");
    }
}
