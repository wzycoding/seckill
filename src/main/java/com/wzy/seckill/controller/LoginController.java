package com.wzy.seckill.controller;

import com.wzy.seckill.result.Result;
import com.wzy.seckill.service.SeckillUserService;
import com.wzy.seckill.vo.LoginVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * 登录Controller
 * @author wzy
 * @version 1.0
 * @date 2019/12/19 8:33
 */
@RequestMapping("/login")
@Controller
@Slf4j
public class LoginController {

    @Autowired
    private SeckillUserService seckillService;

    @RequestMapping("/to_login")
    public String toLogin() {
        return "login";
    }


    @RequestMapping("/do_login")
    @ResponseBody
    public Result<Boolean> doLogin(HttpServletResponse response,
                                   @Valid LoginVo vo) {
        log.info("LoginVo info: {}", vo.toString());
        //登录
        seckillService.login(response, vo);
        return Result.success(true);
    }
}
