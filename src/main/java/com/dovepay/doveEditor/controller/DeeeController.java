package com.dovepay.doveEditor.controller;

import com.alibaba.fastjson.JSON;
import com.dovepay.doveEditor.entity.DeeeAtcl;
import com.dovepay.doveEditor.mapper.DeeeMapper;
import com.dovepay.doveEditor.entity.DeeeUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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
    @GetMapping("/article")
    public void getArticle(HttpServletResponse response) throws IOException {
        setJson(response);
        try {
            HashMap<String, Object> result = new HashMap<>();
            List<DeeeAtcl> list = deeeMapper.getDeeeAtclList();
            result.put("msg", "success");
            result.put("articleList", list);
            response.getWriter().write(JSON.toJSONString(result));
        } catch (Exception e) {
            exceptionHandler(e, response);
        }
    }
    @PutMapping("/article")
    public void insertArticle(@RequestBody Map<String, String> map, HttpServletResponse response) throws IOException {
        setJson(response);
        try {
            HashMap<String, Object> result = new HashMap<>();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            String currentTime = simpleDateFormat.format(new Date());
            Timestamp timeCreate = Timestamp.valueOf(currentTime);
            String version = map.getOrDefault("version", "1.0.0");
            String articleId = map.getOrDefault("id", String.valueOf(deeeMapper.getDeeeAtclRowCount() + 1));
            String title = map.getOrDefault("title", "");
            String author = map.getOrDefault("author", "");
            String editor = map.getOrDefault("editor", "");
            String content = map.getOrDefault("content", "");
            if (title.equals("")) {
                result.put("msg", "error");
                result.put("position", "title");
                result.put("error", "标题不能为空");
                response.setStatus(406);
                response.getWriter().write(JSON.toJSONString(result));
                return;
            }
            if (author.equals("")) {
                result.put("msg", "error");
                result.put("position", "author");
                result.put("error", "作者不能为空");
                response.setStatus(406);
                response.getWriter().write(JSON.toJSONString(result));
                return;
            }
            if (editor.equals("")) {
                result.put("msg", "error");
                result.put("position", "editor");
                result.put("error", "编辑者不能为空");
                response.setStatus(406);
                response.getWriter().write(JSON.toJSONString(result));
                return;
            }
            if (content.equals("")) {
                result.put("msg", "error");
                result.put("position", "content");
                result.put("error", "内容不能为空");
                response.setStatus(406);
                response.getWriter().write(JSON.toJSONString(result));
                return;
            }
            DeeeAtcl deeeAtcl = new DeeeAtcl(title, author, editor, version, articleId, content, timeCreate, timeCreate);
            deeeMapper.insertDeeeAtcl(deeeAtcl);
            result.put("msg", "success");
            result.put("newArticle", deeeAtcl);
            response.getWriter().write(JSON.toJSONString(result));
        } catch (Exception e) {
            exceptionHandler(e, response);
        }
    }
    @DeleteMapping("/article")
    public void deleteArticle(@RequestParam(value = "id", required = false) String id, HttpServletResponse response) throws IOException {
        HashMap<String, Object> result = new HashMap<>();
        setJson(response);
        if (id == null || id.equals("")) {
            result.put("msg", "Not Acceptable");
            result.put("reason", "id为空");
            response.setStatus(406);
            response.getWriter().write(JSON.toJSONString(result));
            return;
        }
        try {
            deeeMapper.deleteDeeeAtclById(id);
            result.put("msg", "success");
            result.put("description", "删除id为"+id+"的文章");
            response.getWriter().write(JSON.toJSONString(result));
        } catch (Exception e) {
            result.put("msg", "failed");
            result.put("errorMsg", e);
            response.setStatus(500);
            response.getWriter().write(JSON.toJSONString(result));
        }
    }
    @PatchMapping("/article")
    public void patchArticle(@RequestParam Map<String, String> requestParam, @RequestBody Map<String, Object> requestBody) {
        deeeMapper.updateDeeeAtcl(requestParam.get("id"), requestBody);
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
    private void setJson(HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
    }
    private void exceptionHandler(Exception e, HttpServletResponse response) throws IOException {
        HashMap<String, Object> exception = new HashMap<>();
        exception.put("msg", "failed");
        exception.put("errorMsg", e);
        response.setStatus(406);
        response.getWriter().write(JSON.toJSONString(exception));
    }
}
