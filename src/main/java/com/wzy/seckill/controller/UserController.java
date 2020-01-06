package com.wzy.seckill.controller;

import com.google.common.util.concurrent.RateLimiter;
import com.wzy.seckill.domain.SeckillUser;
import com.wzy.seckill.result.CodeMsg;
import com.wzy.seckill.result.Result;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;

/**
 * 测试获取用户信息接口，压测限流测试类
 */
@Controller
@RequestMapping("/user")
public class UserController {

    private static final RateLimiter rateLimiter = RateLimiter.create(2);

    @RequestMapping("/info")
    @ResponseBody
    public Result<SeckillUser> info(Model model, SeckillUser user) {
        if (rateLimiter.tryAcquire()) { //一次拿一个令牌
            System.out.println(LocalDateTime.now());
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return Result.success(user);
        } else {
            System.out.println("limit");
            return Result.error(CodeMsg.FREQUENT_VISITS);
        }
    }
}
