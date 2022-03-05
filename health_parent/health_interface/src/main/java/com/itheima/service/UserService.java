package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.pojo.Menu;
import com.itheima.pojo.User;

import java.util.List;

public interface UserService {
    User findUserByusername(String username);

    List<Menu> showMenuByUserId(Integer id);

    PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString);

    void add(User user, Integer[] roles);

    void deleteById(Integer id);

    User findById(Integer id);

    void edit(User user, Integer[] roles);

    Integer queryIdByUsername(String username);
}
