package com.wzy.seckill.controller;

import com.alibaba.fastjson.JSON;
import com.wzy.seckill.domain.SeckillUser;
import com.wzy.seckill.redis.GoodsKey;
import com.wzy.seckill.redis.RedisService;
import com.wzy.seckill.result.Result;
import com.wzy.seckill.service.GoodsService;
import com.wzy.seckill.vo.GoodsDetailVo;
import com.wzy.seckill.vo.GoodsVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    /**
     * QPS: 1677.9
     * QPS: 2165(页面缓存后)
     * 5000 * 2
     * todo:页面缓存后Jmeter测试有问题
     */
    @RequestMapping(value = "/to_list", produces = "text/html")
    @ResponseBody
    public String toList(HttpServletRequest request,
                         HttpServletResponse response,
                         Model model,
                         SeckillUser seckillUser) {

        //取缓存
        String html = redisService.get(GoodsKey.goodsList, "", String.class);
        if (StringUtils.isNotBlank(html)) {
            return html;
        }
        model.addAttribute("user", seckillUser);
        //查询商品列表
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        log.info("goodsList:" + JSON.toJSONString(goodsList));
        model.addAttribute("goodsList", goodsList);

        WebContext ctx = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goods_list", ctx);
        if (StringUtils.isNotBlank(html)) {
            redisService.set(GoodsKey.goodsList, "", html);
        }

        return html;
    }

    @RequestMapping(value = "/detail/{goodsId}")
    @ResponseBody
    public Result<GoodsDetailVo> detail(@PathVariable("goodsId") long goodsId, SeckillUser user) {
        //todo:应该通过雪花算法生成id
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
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
        GoodsDetailVo goodsDetailVo = new GoodsDetailVo();
        goodsDetailVo.setGoodsVo(goods);
        goodsDetailVo.setSeckillStatus(seckillStatus);
        goodsDetailVo.setRemainSeconds(remainSeconds);
        goodsDetailVo.setUser(user);
        return Result.success(goodsDetailVo);
    }

    @RequestMapping(value = "/to_detail2/{goodsId}", produces = "text/html")
    @ResponseBody
    public String detail2(HttpServletRequest request,
                         HttpServletResponse response,
                         Model model,
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

        String html = redisService.get(GoodsKey.goodsDetail, "" + goodsId, String.class);
        if (StringUtils.isNotBlank(html)) {
            return html;
        }
        WebContext ctx = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goods_detail", ctx);
        if (StringUtils.isNotBlank(html)) {
            redisService.set(GoodsKey.goodsDetail, "" + goodsId, html);
        }
        return html;
    }

    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;

    @Autowired
    private RedisService redisService;

    @Resource
    private GoodsService goodsService;


}
