package com.dovepay.doveEditor.filter;

import com.alibaba.fastjson.JSON;
import com.dovepay.doveEditor.response.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Objects;

@WebFilter(urlPatterns = "/dove-eee-data/testInterceptor", filterName = "redisSessionFilter")
//@Order(1)
public class RedisSessionFilter implements Filter {

    private String browserCookieValue;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest)request;
        HttpServletResponse res = (HttpServletResponse)response;
        Cookie[] cookies = req.getCookies();
        if (cookies == null || cookies.length == 0) {
            ErrorResponse err = new ErrorResponse(401, "NoCookies", "请求要求使用用户Cookie");
            sendError(res, err);
            return;
        }
        for (Cookie cookie : cookies) {
            if (Objects.equals(cookie.getName(), "dove.eee.uid")) {
                browserCookieValue = cookie.getValue();
                break;
            }
        }
        if (browserCookieValue == null) {
            ErrorResponse err = new ErrorResponse(401, "Unauthorized", "请求要求用户的身份认证");
            sendError(res, err);
            return;
        }
        String uuid = URLDecoder.decode(browserCookieValue, "UTF-8").split(":", 2)[1].split("\\.", 2)[0];
        String redisProperty = redisTemplate.opsForValue().get("DOVEPAY:DOVE_EEE:USER:" + uuid);
        if (redisProperty == null) {
            ErrorResponse err = new ErrorResponse(401, "SessionExpired", "登录超时, 请重新登录");
            sendError(res, err);
            return;
        }
        if (Objects.requireNonNull(JSON.parseObject(redisProperty)).get("username") == null) {
            ErrorResponse err = new ErrorResponse(401, "Unauthorized", "您已退出, 请重新登录");
            sendError(res, err);
            return;
        }
        chain.doFilter(request, response);
    }

    private void sendError(HttpServletResponse response, ErrorResponse errorResponse) throws IOException {
        response.setStatus(errorResponse.getCode());
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().write(JSON.toJSONString(errorResponse));
        response.getWriter().close();
    }
}