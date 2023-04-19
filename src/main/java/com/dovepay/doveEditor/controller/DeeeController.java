package com.dovepay.doveEditor.controller;

import com.alibaba.fastjson.JSON;
import com.dovepay.doveEditor.entity.DeeeAtcl;
import com.dovepay.doveEditor.mapper.DeeeMapper;
import com.dovepay.doveEditor.entity.DeeeUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@CrossOrigin
@RequestMapping("/dove-eee-data")
public class DeeeController {
    private StringRedisTemplate redisTemplate;
    @Autowired
    public void setRedisTemplate(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    @Resource
    private DeeeMapper deeeMapper;
    @PostMapping("/getUserByName")
    public DeeeUser getUser(@RequestBody Map<String, Object> map) {
        return deeeMapper.getDeeeUserByName((String) map.get("name"));
    }
    @PostMapping("/checkUserName")
    public boolean checkUser(@RequestBody Map<String, Object> map) {
        DeeeUser user = deeeMapper.getDeeeUserByName((String) map.get("name"));
        if (user != null && user.getActive() == 1) return true;
        return false;
    }
    @PostMapping("/checkPassword")
    public boolean checkPassword(@RequestBody Map<String, Object> map) {
        DeeeUser user = deeeMapper.getDeeeUserByName((String) map.get("username"));
        if (user == null) return false;
        if (map.get("password").equals(user.getPwd())) return true;
        return false;
    }
    @PutMapping("/article")
    public Map<String, Object> insertArticle(@RequestBody Map<String, String> map) {
        HashMap<String, Object> response = new HashMap<>();
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
            String currentTime = simpleDateFormat.format(new Date());
            Timestamp timeCreate = Timestamp.valueOf(currentTime);
            String version = map.getOrDefault("version", "1.0.0");
            String articleId = map.getOrDefault("id", String.valueOf(deeeMapper.getDeeeAtclRowCount() + 1));
            DeeeAtcl deeeAtcl = new DeeeAtcl(map.get("title"), map.get("author"), map.get("editor"), version, articleId, map.get("content"),timeCreate, timeCreate);
            deeeMapper.insertDeeeAtcl(deeeAtcl);
            response.put("msg", "success");
            response.put("newArticle", deeeAtcl);
            return response;
        } catch (Exception e) {
            response.put("msg", "failed");
            response.put("errorMsg", e);
            return response;
        }
    }
    @DeleteMapping("/article")
    public Map<String, Object> deleteArticle(@RequestBody Map<String, String> map) {
        HashMap<String, Object> response = new HashMap<>();
        try {
            deeeMapper.deleteDeeeAtclById(map.get("id"));
            response.put("msg", "success");
            return response;
        } catch (Exception e) {
            response.put("msg", "failed");
            response.put("errorMsg", e);
            return response;
        }
    }
    @PatchMapping("/article")
    public Map<String, Object> patchArticle(@RequestBody Map<String, String> map) {
        HashMap<String, Object> response = new HashMap<>();
        try{

            return response;
        } catch (Exception e) {
            response.put("msg", "failed");
            response.put("errorMsg", e);
            return response;
        }
    }
    @GetMapping("/testSess")
    public Map<String, Object> responseSession(@CookieValue(required = false, name = "dove.eee.uid") String cookie) {
        HashMap<String, Object> response = new HashMap<>();
        if (cookie == null) {
            response.put("msg", "未登录");
            return response;
        }
        String uuid = cookie.split(":", 2)[1].split("\\.", 2)[0];
        String redisProperty = redisTemplate.opsForValue().get("DOVEPAY:DOVE_EEE:USER:" + uuid);
        if (Objects.requireNonNull(JSON.parseObject(redisProperty)).get("username") == null) {
            response.put("msg", "未登录");
            return response;
        }
        response.put("msg", "success");
        response.put("username", JSON.parseObject(redisProperty).get("username"));
        return response;
    }
}
