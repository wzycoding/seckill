package com.wzy.seckill.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 返回结果类
 * @author wzy
 * @version 1.0
 * @date 2019/12/16 16:45
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {
    /**
     * 返回信息
     */
    private String msg;
    /**
     * 返回错误码
     */
    private int code;
    /**
     * 返回数据
     */
    private T data;

    /**
     * 返回成功
     */
    public static <T> Result<T> success(T data) {
        return new Result<T>(data);
    }

    /**
     * 返回失败
     */
    public static <T> Result<T> error(CodeMsg codeMsg) {
        return new Result<T>(codeMsg);
    }

    public Result(T data) {
        this.data = data;
    }

    public Result(CodeMsg codeMsg) {
        if (codeMsg != null) {
            this.code = codeMsg.getCode();
            this.msg = codeMsg.getMsg();
        }
    }


}
