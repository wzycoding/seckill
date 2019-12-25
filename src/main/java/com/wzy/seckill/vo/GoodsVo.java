package com.wzy.seckill.vo;

import com.wzy.seckill.domain.Goods;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author wzy
 * @version 1.0
 * @date 2019/12/19 21:26
 */
@Data
public class GoodsVo extends Goods{

    private BigDecimal seckillPrice;

    private Integer stockCount;

    private Date startDate;

    private Date endDate;
}
