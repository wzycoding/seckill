package com.wzy.seckill.controller;

import com.wzy.seckill.domain.OrderInfo;
import com.wzy.seckill.domain.SeckillOrder;
import com.wzy.seckill.domain.SeckillUser;
import com.wzy.seckill.redis.RedisService;
import com.wzy.seckill.result.CodeMsg;
import com.wzy.seckill.result.Result;
import com.wzy.seckill.service.GoodsService;
import com.wzy.seckill.service.OrderService;
import com.wzy.seckill.service.SeckillService;
import com.wzy.seckill.vo.GoodsVo;
import com.wzy.seckill.vo.OrderInfoVo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * 秒杀Controller
 */
@Controller
@RequestMapping("/seckill")
public class SeckillController {
    /**
     * 秒杀商品接口
     */
    @RequestMapping(value = "/do_seckill", method = RequestMethod.POST)
    @ResponseBody
    public Result<OrderInfoVo> doSeckill(SeckillUser user,
                                    @RequestParam("goodsId")long goodsId) {
        if (user == null) {
            return Result.error(CodeMsg.NOT_LOGIN);
        }
        //判断库存
        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = goodsVo.getGoodsStock();
        if (stock <= 0) {
            return Result.error( CodeMsg.STOCK_OVER);
        }
        //判断是否已经秒杀到了
        SeckillOrder order = orderService.getSeckillOrderByUserIdAndGoodsId(user.getId(), goodsId);
        if (order != null) {
            return Result.error(CodeMsg.REPEAT_SECKILL);
        }
        //todo:利用数据库的（userId）唯一索引，来保证同一用户不会重复秒杀到商品，在seckill_order中，并且使用验证码方式来防止一个用户同事发出两个请求
        //减库存、下订单、写入秒杀订单
        OrderInfo orderInfo = seckillService.seckill(user, goodsVo);
        OrderInfoVo orderInfoVo = new OrderInfoVo();
        orderInfoVo.setUser(user);
        orderInfoVo.setGoods(goodsVo);
        orderInfoVo.setOrderInfo(orderInfo);
        return Result.success(orderInfoVo);
    }

    @Resource
    private GoodsService goodsService;

    @Resource
    private RedisService redisService;

    @Resource
    private OrderService orderService;

    @Resource
    private SeckillService seckillService;
}
