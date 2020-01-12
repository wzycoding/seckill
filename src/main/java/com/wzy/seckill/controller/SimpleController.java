package com.wzy.seckill.controller;

import com.wzy.seckill.domain.User;
import com.wzy.seckill.rabbitmq.MQSender;
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

    @Autowired
    private MQSender sender;

//    /**
//     * 测试rabbitmq直连交换机
//     * 适用场景：有优先级的任务，根据任务的优先级把消息发送到对应的队列，这样可以派更多的资源去处理高优先级的队列。
//     */
//    @RequestMapping("/mq/direct")
//    @ResponseBody
//    public Result<String> directMQ() {
//        sender.sendDirectExchange("hello imooc");
//        return Result.success("hello imooc");
//    }
//
//
//    /**
//     * 测试rabbitmq扇形交换机
//     * 适用场景：需要给所有绑定该交换机的队列直接发送消息时使用。
//     */
//    @RequestMapping("/mq/fanout")
//    @ResponseBody
//    public Result<String> fanoutMQ() {
//        sender.sendFanoutExchange("hello imooc");
//        return Result.success("hello imooc");
//    }
//
//    /**
//     * 测试rabbitmq主题交换机
//     * 使用场景：快速绑定到多个队列上
//     */
//    @RequestMapping("/mq/topic")
//    @ResponseBody
//    public Result<String> topicMQ() {
//        sender.sendTopicExchange("hello imooc");
//        return Result.success("hello imooc");
//    }
//
//    /**
//     * 测试rabbitmq主题交换机
//     * 使用场景：快速绑定到多个队列上
//     */
//    @RequestMapping("/mq/headers")
//    @ResponseBody
//    public Result<String> headersMQ() {
//        sender.sendHeadersExchange("hello imooc");
//        return Result.success("hello imooc");
//    }

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
