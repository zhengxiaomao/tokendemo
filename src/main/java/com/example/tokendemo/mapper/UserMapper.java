package com.example.tokendemo.mapper;

import com.example.tokendemo.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface UserMapper {

    User findByUsername(String username);

    User findUserById(String id);

}
