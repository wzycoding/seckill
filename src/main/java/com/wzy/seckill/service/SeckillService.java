package com.wzy.seckill.service;

import com.wzy.seckill.dao.GoodsDao;
import com.wzy.seckill.dao.OrderDao;
import com.wzy.seckill.domain.OrderInfo;
import com.wzy.seckill.domain.SeckillOrder;
import com.wzy.seckill.domain.SeckillUser;
import com.wzy.seckill.redis.GoodsKey;
import com.wzy.seckill.redis.RedisService;
import com.wzy.seckill.redis.SeckillKey;
import com.wzy.seckill.util.MD5Util;
import com.wzy.seckill.vo.GoodsVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.UUID;

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

    public BufferedImage createVerifyCode(SeckillUser user, long goodsId) {
        if(user == null || goodsId <=0) {
            return null;
        }
        int width = 100;
        int height = 32;
        //create the image
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        // set the background color
        g.setColor(new Color(0xDCDCDC));
        g.fillRect(0, 0, width, height);
        // draw the border
        g.setColor(Color.black);
        g.drawRect(0, 0, width - 1, height - 1);
        // create a random instance to generate the codes
        Random rdm = new Random();
        // make some confusion
        for (int i = 0; i < 50; i++) {
            int x = rdm.nextInt(width);
            int y = rdm.nextInt(height);
            g.drawOval(x, y, 0, 0);
        }
        // generate a random code
        String verifyCode = generateVerifyCode(rdm);
        g.setColor(new Color(0, 100, 0));
        g.setFont(new Font("Candara", Font.BOLD, 24));
        g.drawString(verifyCode, 8, 24);
        g.dispose();
        //把验证码存到redis中
        int rnd = calc(verifyCode);
        redisService.set(SeckillKey.getSeckillVerifyCode, user.getId()+","+goodsId, rnd);
        //输出图片
        return image;
    }

    private int calc(String exp) {
        try {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("JavaScript");
            return (int) engine.eval(exp);
        } catch (ScriptException e) {
            e.printStackTrace();
            return 0;
        }
    }
    private static char[] ops = new char[] {'+', '-', '*'};

    private String generateVerifyCode(Random rdm) {
        int num1 = rdm.nextInt(10);
        int num2 = rdm.nextInt(10);
        int num3 = rdm.nextInt(10);
        char op1 = ops[rdm.nextInt(3)];
        char op2 = ops[rdm.nextInt(3)];
        String exp = ""+ num1 + op1 + num2 + op2 + num3;
        return exp;
    }

    public boolean checkVerifyCode(SeckillUser user, long goodsId, int verifyCode) {
        Integer calResult = redisService.get(SeckillKey.getSeckillVerifyCode, user.getId() + "," + goodsId, Integer.class);
        return calResult == verifyCode;
    }

    public String createSeckillPath(SeckillUser user, long goodsId) {
        if (user == null || goodsId <= 0) {
            return null;
        }
        String str = MD5Util.md5(UUID.randomUUID() + "123456");
        redisService.set(SeckillKey.getSeckillPath, user.getId() + "_" + goodsId, str);
        return str;
    }

    public boolean checkPath(String path, SeckillUser user, long goodsId) {
        if (user == null || goodsId <= 0) {
            return false;
        }
        String targetPath = redisService.get(SeckillKey.getSeckillPath, user.getId() + "_" + goodsId, String.class);
        return path.equals(targetPath);
    }
}
