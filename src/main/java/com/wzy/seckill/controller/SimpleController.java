package com.wzy.seckill.controller;

import com.wzy.seckill.domain.User;
import com.wzy.seckill.redis.RedisService;
import com.wzy.seckill.redis.UserPrefix;
import com.wzy.seckill.result.Result;
import com.wzy.seckill.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 测试Controller
 * @author wzy
 * @version 1.0
 * @date 2019/12/16 9:55
 */
@Controller
@RequestMapping("/demo")
public class SimpleController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisService redisService;

    @RequestMapping("/thymeleaf")
    public String thymeleaf(Model model) {
        model.addAttribute("name","wzy");
        return "hello";
    }

    @RequestMapping("/db/get")
    @ResponseBody
    public Result<User> dbGet() {
        User user = userService.getById(1);
        return Result.success(user);
    }

    @RequestMapping("/db/tx")
    @ResponseBody
    public Result<Boolean> dbTx() {
        userService.tx();
        return Result.success(true);
    }

    @RequestMapping("/redis/get")
    @ResponseBody
    public Result<User> redisGet() {
        User user = redisService.get(UserPrefix.getById, "userKey1", User.class);
        return Result.success(user);
    }

    @RequestMapping("/redis/set")
    @ResponseBody
    public Result<Boolean> redisSet() {
        User user1 = new User();
        user1.setName("wzy");
        user1.setId(1);
        boolean result = redisService.set(UserPrefix.getById, "userKey1", user1);
        return Result.success(result);
    }

}
