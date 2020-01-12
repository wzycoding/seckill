package com.wzy.seckill.rabbitmq;

import com.wzy.seckill.domain.SeckillUser;
import lombok.Data;

/**
 * 秒杀信息实体类
 */
@Data
public class SeckillMessage {
    /**
     * 用户信息
     */
    private SeckillUser user;
    /**
     * 商品id
     */
    private long goodsId;
}
