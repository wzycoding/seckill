package com.wzy.seckill.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单信息表
 */
@Data
public class OrderInfo {
    /**
     * id
     */
    private Long id;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 商品id
     */
    private Long goodsId;
    /**
     * 地址id
     */
    private Long deliveryAddrId;
    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 商品数量
     */
    private Integer goodsCount;
    /**
     * 商品价格
     */
    private BigDecimal goodsPrice;
    /**
     * 订单渠道：IOS、Android、PC
     */
    private Byte orderChannel;
    /**
     * 订单状态
     */
    private Byte status;
    /**
     * 下单时间
     */
    private Date createDate;
    /**
     * 支付时间
     */
    private Date payDate;

}