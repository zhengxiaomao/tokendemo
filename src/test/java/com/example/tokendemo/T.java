package com.example.tokendemo;

import com.example.tokendemo.entity.User;
import com.example.tokendemo.mapper.UserMapper;
import com.example.tokendemo.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import javax.annotation.Resource;


@SpringBootTest
@RunWith(SpringRunner.class)
public class T {

    @Resource
    UserService userService;

    @Test
    public void test(){
       User user= userService.findUserById("1");
        System.out.println(user.getUsername());
    }

    @Resource
    UserMapper userMapper;


    @Test
    public void findUserById() {

        System.out.println(userMapper.findUserById("1"));
    }
}
