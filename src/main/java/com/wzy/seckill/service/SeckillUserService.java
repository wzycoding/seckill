package com.wzy.seckill.service;

import com.wzy.seckill.dao.SeckillUserDao;
import com.wzy.seckill.domain.SeckillUser;
import com.wzy.seckill.exception.GlobalException;
import com.wzy.seckill.redis.RedisService;
import com.wzy.seckill.redis.SeckillUserPrefix;
import com.wzy.seckill.result.CodeMsg;
import com.wzy.seckill.util.MD5Util;
import com.wzy.seckill.util.UUIDUtil;
import com.wzy.seckill.vo.LoginVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * 秒杀用户service：service最好调用别人的service，因为别人的service中可能会有缓存
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

    /**
     * 修改密码方法
     */
    public boolean updatePassword(String token, long userId, String password) {
        SeckillUser user = getById(userId);
        if (user == null) {
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        SeckillUser toUpdateUser = new SeckillUser();
        toUpdateUser.setId(userId);
        toUpdateUser.setPassword(password);
        seckillUserDao.updatePassword(toUpdateUser);
        //todo:这里一定要注意不能先使缓存失效，再更新数据库，因为这样做如果使缓存失效了，这时有一个请求去请求数据库中的数据，这就脏数据，然后在更新数据库，
        //todo:导致缓存和数据库的数据不一致，所以一定要先更新数据库更新成功之后再更新缓存。
        //修改缓存
        user.setPassword(password);
        //更新token对应的用户信息
        redisService.set(SeckillUserPrefix.token, token, user);
        //删除根据userId缓存的用户信息
        redisService.del(SeckillUserPrefix.getById, "" + userId);
        return true;
    }

    public SeckillUser getById(long userId) {
        //取缓存,进行最细粒度的对象级别的缓存
        SeckillUser seckillUser = redisService.get(SeckillUserPrefix.getById, "" + userId, SeckillUser.class);
        if (seckillUser != null) {
            return seckillUser;
        }
        seckillUser = seckillUserDao.getById(userId);
        if (seckillUser != null) {
            redisService.set(SeckillUserPrefix.getById, "" + userId, seckillUser);
        }
        return seckillUser;
    }

    public boolean login(HttpServletResponse response, LoginVo vo) {
        if (vo == null) {
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        //拿到电话号码
        String mobile = vo.getMobile();
        //拿到密码
        String password = vo.getPassword();

        SeckillUser seckillUser = seckillUserDao.getById(Long.parseLong(mobile));

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
