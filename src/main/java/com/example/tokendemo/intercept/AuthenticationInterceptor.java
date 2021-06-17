package com.example.tokendemo.intercept;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.tokendemo.anno.PassToken;
import com.example.tokendemo.anno.UserLoginToken;
import com.example.tokendemo.entity.User;
import com.example.tokendemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.lang.reflect.Method;



public class AuthenticationInterceptor implements HandlerInterceptor {



    @Autowired
    UserService userService;


    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) throws Exception {
        String cookie = httpServletRequest.getHeader("Cookie");
        String token=cookie.substring(6);
        System.out.println(token);
        // 如果不是映射到方法直接通过
        if(!(object instanceof HandlerMethod)){
            return true;
        }
        HandlerMethod handlerMethod=(HandlerMethod)object;
        Method method=handlerMethod.getMethod();
        //检查是否有passtoken注释，有则跳过认证
        if (method.isAnnotationPresent(PassToken.class)) {
            PassToken passToken = method.getAnnotation(PassToken.class);
            if (passToken.required()) {
                return true;
            }
        }

        //检查有没有需要用户权限的注解
        if (method.isAnnotationPresent(UserLoginToken.class)) {
            UserLoginToken userLoginToken = method.getAnnotation(UserLoginToken.class);
            if (userLoginToken.required()) {
                // 执行认证
                if (token == null) {
                    throw new RuntimeException("无token，请重新登录");
                }

                String userId;
                try {
                    String user=httpServletRequest.getParameter("username");
                    userId = JWT.decode(token).getAudience().get(0);
                    User user1 = userService.findUserById(userId);
                    if (user1 == null) {
                        httpServletResponse.setCharacterEncoding("UTF-8");
                        httpServletResponse.setContentType("application/json; charset=utf-8");
                        PrintWriter out = null;
                        JSONObject res = new JSONObject();
                        res.put("code",1020);
                        res.put("msg","用户name不存在，请重新登录");
                        out = httpServletResponse.getWriter();
                        out.append(res.toString());
                        return false;
                    }
                    // 验证 token
                    JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user1.getPassword())).build();
                    try {
                        jwtVerifier.verify(token);
                    } catch (JWTVerificationException e) {
                        throw new RuntimeException("401");
                    }

                } catch (JWTDecodeException j) {
                    throw new RuntimeException("401");
                }


            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
