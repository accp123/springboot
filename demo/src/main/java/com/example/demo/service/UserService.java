package com.example.demo.service;

import com.example.demo.entity.User;

import java.util.List;

public interface UserService {
    public User getById(Integer id);

    public void insert(User user);

    public List<User> getList();

    public void deleteById(Integer id);

    public void edit(User user);

    public User getByName(String username);
}
