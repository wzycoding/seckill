package com.wzy.seckill.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 秒杀商品表
 */
@Data
public class SeckillGoods {
    /**
     * id
     */
    private Long id;
    /**
     * 商品id
     */
    private Long goodsId;
    /**
     * 秒杀价格
     */
    private BigDecimal seckillPrice;
    /**
     * 库存数量
     */
    private Integer stockCount;
    /**
     * 开始时间
     */
    private Date startDate;
    /**
     * 结束时间
     */
    private Date endDate;

}