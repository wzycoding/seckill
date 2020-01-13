package com.wzy.seckill.controller;

import com.wzy.seckill.access.AccessLimit;
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
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 秒杀Controller
 */
@Controller
@RequestMapping("/seckill")
public class SeckillController implements InitializingBean {

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

    public Map<Long, Boolean> localOverMap = new HashMap<>();

    /**
     * 秒杀商品接口
     */
    @AccessLimit(seconds = 5, maxCount = 5)
    @PostMapping(value = "/{path}/do_seckill")
    @ResponseBody
    public Result<Integer> doSeckill(SeckillUser user,
                                    @RequestParam("goodsId")long goodsId,
                                     @PathVariable("path") String path) {
        if (user == null) {
            return Result.error(CodeMsg.NOT_LOGIN);
        }
        //检查路径是否正确
        boolean result = seckillService.checkPath(path, user, goodsId);
        if (!result) {
            return Result.error(CodeMsg.REQUEST_ILLEGAL);
        }
        boolean isOver = localOverMap.get(goodsId);
        if (isOver) {
            return Result.error(CodeMsg.STOCK_OVER);
        }
        //判断是否已经秒杀到了
        SeckillOrder order = orderService.getSeckillOrderByUserIdAndGoodsId(user.getId(), goodsId);
        if (order != null) {
            return Result.error(CodeMsg.REPEAT_SECKILL);
        }
        //预减库存
        long stock = redisService.decr(GoodsKey.getSeckillGoodsStock, "" + goodsId);
        //如果减完之后小于0则秒杀失败
        if (stock < 0) {
            localOverMap.put(goodsId, true);
            return Result.error(CodeMsg.STOCK_OVER);
        }

        SeckillMessage msg = new SeckillMessage();
        msg.setGoodsId(goodsId);
        msg.setUser(user);
        //入队
        sender.sendSeckillMessage(msg);

        return Result.success(0);
        //todo:利用数据库的（userId）唯一索引，来保证同一用户不会重复秒杀到商品，在seckill_order中，并且使用验证码方式来防止一个用户同事发出两个请求
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

    @GetMapping(value = "/verify")
    @ResponseBody
    public Result<String> getVerifyCode(HttpServletResponse response, SeckillUser user,@Param("goodsId") long goodsId) {
        if (user == null) {
            Result.error(CodeMsg.NOT_LOGIN);
        }
        try {
            BufferedImage image = seckillService.createVerifyCode(user, goodsId);
            OutputStream out = response.getOutputStream();
            ImageIO.write(image, "JPEG", out);
            out.flush();
            out.close();
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return Result.error(CodeMsg.SECKILL_FAIL);
        }
    }

    @AccessLimit(maxCount = 5, seconds = 5)
    @GetMapping(value = "/path")
    @ResponseBody
    public Result<String> getSeckillPath(SeckillUser user, @RequestParam("goodsId") long goodsId,
                                         @RequestParam(value = "verifyCode", defaultValue = "0") int verifyCode) {
        if (user == null) {
            return Result.error(CodeMsg.NOT_LOGIN);
        }
        boolean result = seckillService.checkVerifyCode(user, goodsId, verifyCode);
        if (!result) {
            return Result.error(CodeMsg.SECKILL_FAIL);
        }
        String path = seckillService.createSeckillPath(user, goodsId);
        return Result.success(path);
    }

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
            localOverMap.put(goods.getId(), false);
        }
    }
}
