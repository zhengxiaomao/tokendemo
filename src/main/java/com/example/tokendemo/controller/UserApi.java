package com.example.tokendemo.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.tokendemo.anno.UserLoginToken;
import com.example.tokendemo.entity.User;
import com.example.tokendemo.service.TokenService;
import com.example.tokendemo.service.UserService;
import com.example.tokendemo.util.TokenUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
public class UserApi {
    @Autowired
    UserService userService;
    @Autowired
    TokenService tokenService;

    // 登录
    @UserLoginToken
    @ApiOperation(value = "登陆", notes = "登陆")
    @RequestMapping(value = "/login" ,method = RequestMethod.GET)
    public Object login(User user, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        User userForBase = new User();
        userForBase.setId(userService.findByUsername(user.getUsername()).getId());
        userForBase.setUsername(userService.findByUsername(user.getUsername()).getUsername());
        userForBase.setPassword(userService.findByUsername(user.getUsername()).getPassword());
        if (!userForBase.getPassword().equals(user.getPassword())) {
            jsonObject.put("message", "登录失败,密码错误");
            return jsonObject;
        } else {
            String token = tokenService.getToken(userForBase);
            jsonObject.put("token", token);

            Cookie cookie = new Cookie("token", token);
            cookie.setPath("/");
            response.addCookie(cookie);
            return jsonObject;

        }
    }
    /***
     * 这个请求需要验证token才能访问
     *
     * @author: qiaoyn
     * @date 2019/06/14
     * @return String 返回类型
     */
    @UserLoginToken
    @ApiOperation(value = "获取信息", notes = "获取信息")
    @RequestMapping(value = "/getMessage" ,method = RequestMethod.GET)
    public String getMessage() {

        // 取出token中带的用户id 进行操作
//        System.out.println(TokenUtil.getTokenUserId());

        return "您已通过验证";
    }
}
