package com.example.tokendemo.service;

import com.example.tokendemo.entity.User;
import com.example.tokendemo.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Component
public class UserService {

    @Resource
    UserMapper userMapper;

    public User findByUsername(String userName){
        return userMapper.findByUsername(userName);
    }
    public User findUserById(String userId) {
        return userMapper.findUserById(userId);
    }
}



