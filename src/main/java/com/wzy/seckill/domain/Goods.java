package com.wzy.seckill.domain;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 商品表
 */
@Data
public class Goods {
    /**
     * 商品id
     */
    private Long id;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品标题
     */
    private String goodsTitle;

    /**
     * 商品图片
     */
    private String goodsImg;

    /**
     * 商品的价格
     */
    private BigDecimal goodsPrice;

    /**
     * 商品的库存
     */
    private Integer goodsStock;

    /**
     * 商品的详情
     */
    private String goodsDetail;

}