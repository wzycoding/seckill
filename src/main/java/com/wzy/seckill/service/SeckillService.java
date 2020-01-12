package com.wzy.seckill.service;

import com.wzy.seckill.dao.GoodsDao;
import com.wzy.seckill.dao.OrderDao;
import com.wzy.seckill.domain.OrderInfo;
import com.wzy.seckill.domain.SeckillOrder;
import com.wzy.seckill.domain.SeckillUser;
import com.wzy.seckill.redis.GoodsKey;
import com.wzy.seckill.redis.RedisService;
import com.wzy.seckill.vo.GoodsVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class SeckillService {
    @Resource
    private OrderDao orderDao;

    @Resource
    private GoodsDao goodsDao;

    @Resource
    private GoodsService goodsService;

    @Resource
    private OrderService orderService;

    @Resource
    private RedisService redisService;

    @Transactional
    public OrderInfo seckill(SeckillUser user, GoodsVo goodsVo) {
        //减库存
        boolean result = goodsService.reduceStock(goodsVo);

        //下订单 order_info seckill_order
        //写入秒杀订单
        if (result) {
            return orderService.createOrder(user, goodsVo);
        } else {
            setStockOver(goodsVo.getId());
            return null;
        }

    }

    private void setStockOver(Long goodsId) {
        redisService.set(GoodsKey.getGoodsOver, "" + goodsId, true);
    }



    public long getSeckillResult(SeckillUser user, long goodsId) {
        SeckillOrder seckillOrder = orderService.getSeckillOrderByUserIdAndGoodsId(user.getId(), goodsId);
        if (seckillOrder != null) {
            //成功返回订单id
            return seckillOrder.getOrderId();
        } else {
            boolean isOver = getStockOver(goodsId);
            if (isOver) {
                //失败
                return -1;
            } else {
                //继续等待
                return 0;
            }
        }
    }

    private boolean getStockOver(long goodsId) {
        return redisService.exists(GoodsKey.getGoodsOver, "" + goodsId);
    }

}
