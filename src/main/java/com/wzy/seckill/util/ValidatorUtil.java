package com.wzy.seckill.util;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 参数格式校验工具
 * @author wzy
 * @version 1.0
 * @date 2019/12/19 10:31
 */
public class ValidatorUtil {

    public static final Pattern MOBILE_PATTERN = Pattern.compile("1\\d{10}");
    public static boolean isMobile(String src) {
        if (StringUtils.isBlank(src)) {
            return false;
        }

        Matcher m = MOBILE_PATTERN.matcher(src);
        return m.matches();
    }

    public static void main(String[] args) {
        String mobile = "22297494749";
        boolean result = isMobile(mobile);
        System.out.println(result);
    }
}
