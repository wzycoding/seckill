package com.wzy.seckill.vo;

import com.wzy.seckill.domain.OrderInfo;
import com.wzy.seckill.domain.SeckillUser;
import lombok.Data;

@Data
public class OrderInfoVo {
    SeckillUser user;
    GoodsVo goods;
    OrderInfo orderInfo;
}
