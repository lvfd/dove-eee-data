package com.dovepay.doveEditor.filter;

import com.alibaba.fastjson.JSON;
import com.dovepay.doveEditor.response.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

@WebFilter(urlPatterns = {"/dove-eee-data/testSess", "/dove-eee-data/article", "/dove-eee-data/articleList"}, filterName = "redisSessionFilter")
//@Order(1)
public class RedisSessionFilter implements Filter {

    private String browserCookieValue;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Value("${frontend-host}")
    String frontendHost;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest)request;
        HttpServletResponse res = (HttpServletResponse)response;
        res.setHeader("Access-Control-Allow-Origin", frontendHost == null? "*": frontendHost);
        res.setHeader("Access-Control-Allow-Credentials", "true");
        res.setHeader("Access-Control-Allow-Headers", "Content-Type,Content-Length, Authorization, Accept,X-Requested-With,X-App-Id, X-Token");
        res.setHeader("Access-Control-Allow-Methods","PUT,POST,GET,DELETE,OPTIONS,PATCH");
        res.setHeader("Access-Control-Max-Age", "3600");
        if (req.getMethod().equals("OPTIONS")) {
            res.setStatus(200);
            return;
        }
        Cookie[] cookies = req.getCookies();
        if (cookies == null || cookies.length == 0) {
            ErrorResponse err = new ErrorResponse(401, "NoCookies", "请求要求使用用户Cookie");
            ErrorResponse.sendError(res, err);
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
            ErrorResponse.sendError(res, err);
            return;
        }
        String uuid = URLDecoder.decode(browserCookieValue, "UTF-8").split(":", 2)[1].split("\\.", 2)[0];
        String redisProperty = redisTemplate.opsForValue().get("DOVEPAY:DOVE_EEE:USER:" + uuid);
        if (redisProperty == null) {
            ErrorResponse err = new ErrorResponse(401, "SessionExpired", "登录超时, 请重新登录");
            ErrorResponse.sendError(res, err);
            return;
        }
        if (Objects.requireNonNull(JSON.parseObject(redisProperty)).get("username") == null) {
            ErrorResponse err = new ErrorResponse(401, "Unauthorized", "您已退出, 请重新登录");
            ErrorResponse.sendError(res, err);
            return;
        }
        chain.doFilter(request, response);
    }
}