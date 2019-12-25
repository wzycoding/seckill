package com.wzy.seckill.util;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * MD5工具类
 * @author wzy
 * @version 1.0
 * @date 2019/12/17 11:11
 */
public class MD5Util {
    private static final String salt = "1a2b3c4d";
    public static String md5(String src) {
        return DigestUtils.md5Hex(src);
    }

    public static String inputPassFormPass(String inputPass) {
        String str = "" + salt.charAt(0) + salt.charAt(2) + inputPass + salt.charAt(5) + salt.charAt(4);
        return md5(str);
    }
    public static String formPassToDBPass(String inputPass, String salt) {
        String str = "" + salt.charAt(0) + salt.charAt(2) + inputPass + salt.charAt(5) + salt.charAt(4);
        return md5(str);
    }

    public static String inputPassToDB(String input, String saltDB) {
        String formPass = inputPassFormPass(input);
        String dbPass = formPassToDBPass(formPass, saltDB);
        return dbPass;
    }

    public static void main(String[] args) {
        System.out.println(inputPassFormPass("123456"));
//        System.out.println(formPassToDBPass(inputPassFormPass("123456"), "1a2b3c4d"));
//        System.out.println(inputPassToDB("123456", "1a2b3c4d"));
    }
}
