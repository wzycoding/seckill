package com.wzy.seckill.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 错误码信息类
 * @author wzy
 * @version 1.0
 * @date 2019/12/16 16:51
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CodeMsg {

    /**
     * 错误码
     */
    private int code;

    /**
     * 错误信息
     */
    private String msg;

    //通用错误码5001xx
    public static CodeMsg SUCCESS = new CodeMsg(0, "success");
    public static CodeMsg SERVER_ERROR = new CodeMsg(500100, "服务器端异常");
    public static CodeMsg BIND_ERROR = new CodeMsg(500101, "参数校验异常：%s");

    //登录模块5002xx
    public static CodeMsg SESSION_ERROR = new CodeMsg(500200, "Session不存在或者已经失效");
    public static CodeMsg PASSWORD_EMPTY = new CodeMsg(500201, "登录密码不能为空");
    public static CodeMsg MOBILE_EMPTY = new CodeMsg(500202, "登录手机号不能为空");

    public static CodeMsg MOBILE_ERROR = new CodeMsg(500203, "手机号码格式错误");
    public static CodeMsg MOBILE_NOT_EXIST = new CodeMsg(500204, "手机号码不存在");
    public static CodeMsg PASSWORD_ERROR = new CodeMsg(500205, "密码错误");

    //商品模块5003xx


    //订单模块5004xx


    public CodeMsg fillArgs(Object...args) {
        int code = this.code;
        String message = String.format(this.msg, args);
        return new CodeMsg(code, message);
    }


}
