package com.wzy.seckill.service;

import com.wzy.seckill.dao.GoodsDao;
import com.wzy.seckill.dao.OrderDao;
import com.wzy.seckill.domain.Goods;
import com.wzy.seckill.domain.OrderInfo;
import com.wzy.seckill.domain.SeckillUser;
import com.wzy.seckill.vo.GoodsVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class SeckillService {

    @Transactional
    public OrderInfo seckill(SeckillUser user, GoodsVo goodsVo) {
        //减库存
        goodsService.reduceStock(goodsVo);

        //下订单 order_info seckill_order
        //写入秒杀订单
        return orderService.createOrder(user, goodsVo);
    }

    @Resource
    private OrderDao orderDao;

    @Resource
    private GoodsDao goodsDao;

    @Resource
    private GoodsService goodsService;

    @Resource
    private OrderService orderService;

}
