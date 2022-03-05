package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.User;

import java.util.Map;

public interface UserDao {
    User findByusername(String username);

    Integer queryRoleIdByUserId(Integer id);

    Page<User> pageQuery(String queryString);

    void add(User user);

    void setUserAndRole(Map<String, Integer> map);

    long findCountByUserId(Integer id);

    void deleteById(Integer id);

    User findById(Integer id);

    void edit(User user);

    void deleteAssociationByUserId(Integer id);

    Integer queryIdByUsername(String username);
}
