package com.wzy.seckill.domain;

import lombok.Data;

/**
 * 秒杀订单表
 */
@Data
public class SeckillOrder {
    /**
     * id
     */
    private Long id;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 订单id
     */
    private Long orderId;
    /**
     * 商品id
     */
    private Long goodsId;

}