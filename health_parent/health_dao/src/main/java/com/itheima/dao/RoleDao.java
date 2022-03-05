package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.Role;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public interface RoleDao {
    Set<Role> findByuserId(Integer userId);

    List<Integer> queryMenuIdsByRoleId(Integer roleId);

    List<Role> findAll();

    Integer[] findroleIdsByUserId(Integer userId);

    Page<Role> findPage(String queryString);




    void add(Role role);

    void addrole_permission(HashMap<String, Integer> map);

    void addrole_menu(HashMap<String, Integer> map);

    void edit(Role role);

    void deleterole_permission(Integer id);

    void deleterole_menu(Integer id);

    void delete(Integer id);

    List<Integer> findPermissionIdsByRoleId(Integer roleId);

    List<Integer> findMenuIdsByRoleId(Integer roleId);
}
