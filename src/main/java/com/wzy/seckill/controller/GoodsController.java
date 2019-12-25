package com.wzy.seckill.controller;

import com.wzy.seckill.domain.SeckillUser;
import com.wzy.seckill.service.GoodsService;
import com.wzy.seckill.service.SeckillUserService;
import com.wzy.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * 商品Controller
 * @author wzy
 * @version 1.0
 * @date 2019/12/19 14:28
 */
@RequestMapping("/goods")
@Controller
public class GoodsController {

    @Autowired
    private SeckillUserService seckillService;

    @Autowired
    private GoodsService goodsService;

    @RequestMapping("/to_list")
    public String toList(Model model,
                         SeckillUser seckillUser) {
        model.addAttribute("user", seckillUser);

        //查询商品列表
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        model.addAttribute("goodsList", goodsList);
        return "goods_list";
    }


}
