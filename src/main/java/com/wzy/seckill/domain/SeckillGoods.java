package com.wzy.seckill.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class SeckillGoods {
    private Long id;

    private Long goodsId;

    private BigDecimal seckillPrice;

    private Integer stockCount;

    private Date startDate;

    private Date endDate;

}