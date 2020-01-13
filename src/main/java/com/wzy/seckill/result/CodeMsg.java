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
    public static CodeMsg FREQUENT_VISITS = new CodeMsg(500102, "访问过于频繁，请稍后再试");
    public static CodeMsg NOT_LOGIN = new CodeMsg(500103, "您还未登录，请登录后访问");
    public static CodeMsg SECKILL_FAIL = new CodeMsg(500104, "秒杀失败");
    public static CodeMsg REQUEST_ILLEGAL = new CodeMsg(500105, "请求违法");

    //登录模块5002xx
    public static CodeMsg SESSION_ERROR = new CodeMsg(500200, "Session不存在或者已经失效");
    public static CodeMsg PASSWORD_EMPTY = new CodeMsg(500201, "登录密码不能为空");
    public static CodeMsg MOBILE_EMPTY = new CodeMsg(500202, "登录手机号不能为空");

    public static CodeMsg MOBILE_ERROR = new CodeMsg(500203, "手机号码格式错误");
    public static CodeMsg MOBILE_NOT_EXIST = new CodeMsg(500204, "手机号码不存在");
    public static CodeMsg PASSWORD_ERROR = new CodeMsg(500205, "密码错误");

    //商品模块5003xx
    public static CodeMsg GOODS_NOT_EXIST  = new CodeMsg(500400, "商品不存在");

    //订单模块5004xx
    public static CodeMsg ORDER_NOT_EXIST  = new CodeMsg(500400, "订单不存在");


    //秒杀模块5005xx
    public static CodeMsg STOCK_OVER  = new CodeMsg(500500, "商品已经秒杀结束");
    public static CodeMsg REPEAT_SECKILL  = new CodeMsg(500501, "请勿重复秒杀商品");

    public static CodeMsg VERIFY_CODE_FAIL  = new CodeMsg(500502, "印证吗错误");



    public CodeMsg fillArgs(Object...args) {
        int code = this.code;
        String message = String.format(this.msg, args);
        return new CodeMsg(code, message);
    }


}
