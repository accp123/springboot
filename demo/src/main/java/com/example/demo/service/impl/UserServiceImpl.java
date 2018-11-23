package com.example.demo.service.impl;

import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
@Service(value = "userService")
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;
    @Override
    public User getById(Integer id) {
        return this.userMapper.getById(id);
    }

    @Override
    @Transactional
    public void insert(User user) {

        this.userMapper.insert(user);
      //  int num = 5/0;
    }

    @Override
    public List<User> getList() {
        return this.userMapper.getList();
    }

    @Override
    public void deleteById(Integer id) {
        this.userMapper.deleteById(id);
    }

    @Override
    public void edit(User user) {
        this.userMapper.update(user);
    }

    @Override
    public User getByName(String username) {
        return this.userMapper.getByName(username);
    }
}
