package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.Permission;

import java.util.List;
import java.util.Set;

public interface PermissionDao {
    Set<Permission> findByRoleId(Integer roleId);
    Page<Permission> pageQuery(String queryString);

    void add(Permission permission);

    long findCountByPermissionId(Integer id);

    void deleteById(Integer id);

    Permission findById(Integer id);

    void edit(Permission permission);

    List<Permission> findAll();

}
