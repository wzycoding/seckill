package com.wzy.seckill.exception;

import com.wzy.seckill.result.CodeMsg;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author wzy
 * @version 1.0
 * @date 2019/12/19 12:43
 */
@Getter
public class GlobalException extends RuntimeException{

    private CodeMsg codeMsg;

    public GlobalException(CodeMsg codeMsg) {
        super();
        this.codeMsg = codeMsg;
    }
}
