package com.wzy.seckill.controller;

import com.alibaba.fastjson.JSON;
import com.wzy.seckill.domain.SeckillUser;
import com.wzy.seckill.service.GoodsService;
import com.wzy.seckill.service.SeckillUserService;
import com.wzy.seckill.vo.GoodsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.List;

/**
 * 商品Controller
 * @author wzy
 * @version 1.0
 * @date 2019/12/19 14:28
 */
@RequestMapping("/goods")
@Controller
@Slf4j
public class GoodsController {

    @RequestMapping("/to_list")
    public String toList(Model model,
                         SeckillUser seckillUser) {
        model.addAttribute("user", seckillUser);

        //查询商品列表
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        log.info("goodsList:" + JSON.toJSONString(goodsList));
        model.addAttribute("goodsList", goodsList);

        return "goods_list";
    }

    @RequestMapping("/to_detail/{goodsId}")
    public String detail(Model model,
                         SeckillUser seckillUser,
                         @PathVariable("goodsId") long goodsId) {
        model.addAttribute("user", seckillUser);
        //todo:应该通过雪花算法生成id
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        model.addAttribute("goods", goods);
        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();

        //秒杀状态
        int seckillStatus = 0;
        //剩余多少秒
        int remainSeconds = 0;
        if (now < startAt) {//秒杀还没有开始，倒计时
            remainSeconds = (int)(startAt - now) / 1000;
        } else if (now > endAt) {//秒杀已经结束
            seckillStatus = 2;
            remainSeconds = -1;
        } else {//秒杀正在进行中
            seckillStatus = 1;
        }
        model.addAttribute("seckillStatus", seckillStatus);
        model.addAttribute("remainSeconds", remainSeconds);
        model.addAttribute("goods", goods);
        return "goods_detail";
    }

    @Autowired
    private SeckillUserService seckillUserService;
    @Resource
    private GoodsService goodsService;


}
