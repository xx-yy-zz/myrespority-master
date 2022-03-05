package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.pojo.Permission;

import java.util.List;

public interface PermissionService {
    PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString);

    void add(Permission permission);

    void deleteById(Integer id);

    Permission findById(Integer id);

    void edit(Permission permission);

    List<Permission> findAll();
}
