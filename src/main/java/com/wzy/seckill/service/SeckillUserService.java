package com.wzy.seckill.service;

import com.wzy.seckill.dao.SeckillUserDao;
import com.wzy.seckill.domain.SeckillUser;
import com.wzy.seckill.exception.GlobalException;
import com.wzy.seckill.redis.RedisService;
import com.wzy.seckill.redis.SeckillUserPrefix;
import com.wzy.seckill.result.CodeMsg;
import com.wzy.seckill.result.Result;
import com.wzy.seckill.util.MD5Util;
import com.wzy.seckill.util.UUIDUtil;
import com.wzy.seckill.vo.LoginVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * 秒杀用户service
 * @author wzy
 * @version 1.0
 * @date 2019/12/19 11:03
 */
@Service
public class SeckillUserService {

    public static final String COOKIE_NAME_TOKEN = "token";
    @Resource
    private SeckillUserDao seckillUserDao;

    @Resource
    private RedisService redisService;

//    public SeckillUser getById(Long id) {
//        seckillUserDao.getById(id);
//    }

    public boolean login(HttpServletResponse response, LoginVo vo) {
        if (vo == null) {
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        //拿到电话号码
        String mobile = vo.getMobile();
        //拿到密码
        String password = vo.getPassword();

        SeckillUser seckillUser = seckillUserDao.getById(mobile);

        if (seckillUser == null) {
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }

        //验证密码
        String dbPass = seckillUser.getPassword();
        String saltDB = seckillUser.getSalt();

        String calcPass = MD5Util.formPassToDBPass(password, saltDB);
        if (!dbPass.equals(calcPass)) {
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }
        //生成cookie
        String token = UUIDUtil.uuid();
        addCookie(response, seckillUser, token);
        return true;
    }

    public SeckillUser getByToken(HttpServletResponse response, String token) {
        if (StringUtils.isBlank(token)) {
            return null;
        }
        SeckillUser seckillUser =  redisService.get(SeckillUserPrefix.token, token, SeckillUser.class);
        //延长有效期
        if (seckillUser != null) {
            addCookie(response, seckillUser, token);
        }
        return seckillUser;
    }

    private void addCookie(HttpServletResponse response, SeckillUser seckillUser, String token) {
        redisService.set(SeckillUserPrefix.token, token, seckillUser);
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
        cookie.setMaxAge(SeckillUserPrefix.token.expireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
