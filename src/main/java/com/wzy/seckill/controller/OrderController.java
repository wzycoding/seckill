package com.wzy.seckill.controller;

import com.wzy.seckill.domain.OrderInfo;
import com.wzy.seckill.domain.SeckillUser;
import com.wzy.seckill.result.CodeMsg;
import com.wzy.seckill.result.Result;
import com.wzy.seckill.service.GoodsService;
import com.wzy.seckill.service.OrderService;
import com.wzy.seckill.vo.GoodsVo;
import com.wzy.seckill.vo.OrderInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private GoodsService goodsService;

    @GetMapping("/info")
    public Result<OrderInfoVo> getOrderInfo(@RequestParam("orderId") long orderId, SeckillUser user) {
        if (user == null) {
            return Result.error(CodeMsg.NOT_LOGIN);
        }
        OrderInfo orderInfo = orderService.getOrderById(orderId);
        if (orderInfo == null) {
            return Result.error(CodeMsg.ORDER_NOT_EXIST);
        }
        long goodsId = orderInfo.getGoodsId();
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        if (goods == null) {
            return Result.error(CodeMsg.GOODS_NOT_EXIST);
        }
        OrderInfoVo orderInfoVo = new OrderInfoVo();
        orderInfoVo.setGoods(goods);
        orderInfoVo.setOrderInfo(orderInfo);
        orderInfoVo.setUser(user);
        return Result.success(orderInfoVo);
    }
}
