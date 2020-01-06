package com.wzy.seckill.vo;

import com.wzy.seckill.domain.SeckillUser;
import lombok.Data;

@Data
public class GoodsDetailVo {
    private SeckillUser user;
    /**
     * 秒杀状态
     */
    private Integer seckillStatus = 0;
    /**
     * 剩余时间
     */
    private Integer remainSeconds = 0;
    /**
     * 商品信息
     */
    private GoodsVo goodsVo;
}
