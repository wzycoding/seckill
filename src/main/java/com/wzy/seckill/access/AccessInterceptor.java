package com.wzy.seckill.access;

import com.alibaba.fastjson.JSON;
import com.wzy.seckill.domain.SeckillUser;
import com.wzy.seckill.redis.AccessKey;
import com.wzy.seckill.redis.RedisService;
import com.wzy.seckill.result.CodeMsg;
import com.wzy.seckill.service.SeckillUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 接口限流拦截器
 */
@Component
@Slf4j
public class AccessInterceptor extends HandlerInterceptorAdapter {

    /**
     * 引入userService
     */
    @Autowired
    private SeckillUserService userService;

    @Autowired
    private RedisService redisService;
    /**
     * 在方法执行前
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(handler instanceof HandlerMethod) {
            SeckillUser user = getUser(request, response);
            UserContext.setUser(user);
            HandlerMethod method = (HandlerMethod) handler;
            AccessLimit accessLimit = method.getMethodAnnotation(AccessLimit.class);
            if(accessLimit == null) {
                return true;
            }
            int maxCount = accessLimit.maxCount();
            int seconds = accessLimit.seconds();
            boolean needLogin = accessLimit.needLogin();
            String key = request.getRequestURI();

            if (needLogin) {
                if (user == null) {
                    render(response, CodeMsg.NOT_LOGIN);
                    return false;

                }
                key += "_" + user.getId();
            } else {
                //do nothing
            }
            AccessKey ak = AccessKey.withExpire(seconds);
            Integer count= redisService.get(ak, "" + key, Integer.class);
            if (count == null) {
                redisService.set(ak, "" + key, 1);
            } else if(count < maxCount) {
                //增加点击次数
                redisService.incr(ak, key);
            } else {
               render(response, CodeMsg.FREQUENT_VISITS);
               return false;
             }
        }
        return true;
    }

    private void render(HttpServletResponse response, CodeMsg codeMsg) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        OutputStream os = response.getOutputStream();
        String message = JSON.toJSONString(codeMsg);
        os.write(message.getBytes("UTF-8"));
        os.flush();
        os.close();
    }

    private SeckillUser getUser(HttpServletRequest request, HttpServletResponse response) {
        String paramToken = request.getParameter(SeckillUserService.COOKIE_NAME_TOKEN);
        String cookieToken = getCookieValue(request, SeckillUserService.COOKIE_NAME_TOKEN);
        if (StringUtils.isBlank(cookieToken) && StringUtils.isBlank(paramToken)) {
            return null;
        }
        String token = StringUtils.isBlank(paramToken)? cookieToken: paramToken;
        return userService.getByToken(response, token);
    }

    private String getCookieValue(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length <= 0) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookieName)) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
