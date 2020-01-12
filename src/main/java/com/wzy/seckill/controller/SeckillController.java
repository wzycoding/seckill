package com.wzy.seckill.controller;

import com.wzy.seckill.domain.OrderInfo;
import com.wzy.seckill.domain.SeckillOrder;
import com.wzy.seckill.domain.SeckillUser;
import com.wzy.seckill.rabbitmq.MQSender;
import com.wzy.seckill.rabbitmq.SeckillMessage;
import com.wzy.seckill.redis.GoodsKey;
import com.wzy.seckill.redis.RedisService;
import com.wzy.seckill.result.CodeMsg;
import com.wzy.seckill.result.Result;
import com.wzy.seckill.service.GoodsService;
import com.wzy.seckill.service.OrderService;
import com.wzy.seckill.service.SeckillService;
import com.wzy.seckill.vo.GoodsVo;
import com.wzy.seckill.vo.OrderInfoVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 秒杀Controller
 */
@Controller
@RequestMapping("/seckill")
public class SeckillController implements InitializingBean {
    /**
     * 秒杀商品接口
     */
    @PostMapping(value = "/do_seckill")
    @ResponseBody
    public Result<Integer> doSeckill(SeckillUser user,
                                    @RequestParam("goodsId")long goodsId) {
        if (user == null) {
            return Result.error(CodeMsg.NOT_LOGIN);
        }
        //预减库存
        long stock = redisService.decr(GoodsKey.getSeckillGoodsStock, "" + goodsId);
        //如果减完之后小于0则秒杀失败
        if (stock < 0) {
            return Result.error(CodeMsg.STOCK_OVER);
        }

        //判断是否已经秒杀到了
        SeckillOrder order = orderService.getSeckillOrderByUserIdAndGoodsId(user.getId(), goodsId);
        if (order != null) {
            return Result.error(CodeMsg.REPEAT_SECKILL);
        }

        SeckillMessage msg = new SeckillMessage();
        msg.setGoodsId(goodsId);
        msg.setUser(user);
        //入队
        sender.sendSeckillMessage(msg);

        return Result.success(0);
        /*
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
        */
    }

    @GetMapping(value = "/result")
    @ResponseBody
    public Result<Long> result(SeckillUser user,@RequestParam("goodsId") long goodsId) {
        if (user == null) {
            return Result.error(CodeMsg.NOT_LOGIN);
        }
        long result = seckillService.getSeckillResult(user, goodsId);
        return Result.success(result);
    }


    @Resource
    private GoodsService goodsService;

    @Resource
    private RedisService redisService;

    @Resource
    private OrderService orderService;

    @Resource
    private SeckillService seckillService;

    @Autowired
    private MQSender sender;

    /**
     * 如果实现了InitializingBean接口就回去回调这个方法
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        if (goodsList == null) {
            return;
        }
        for (GoodsVo goods : goodsList) {
            redisService.set(GoodsKey.getSeckillGoodsStock, "" + goods.getId(), goods.getStockCount());
        }
    }
}
