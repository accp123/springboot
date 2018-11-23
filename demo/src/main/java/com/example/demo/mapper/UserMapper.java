package com.example.demo.mapper;

import com.example.demo.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface UserMapper {
    public User getById(Integer id);

    public void insert(User user);

    public List<User> getList();

    public void deleteById(Integer id);

    public void update(User user);

    //用户登录【shiro】
    public User getByName(String username);
}
