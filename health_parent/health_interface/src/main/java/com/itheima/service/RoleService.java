package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.Role;

import java.util.HashMap;
import java.util.List;

public interface RoleService {
    List<Role> findAll();

    Integer[] findroleIdsByUserId(Integer userId);

    PageResult findPage(QueryPageBean queryPageBean);

    Result add(Role role,Integer[]permissionIds,Integer[]menuIds);

    Result edit(Role role, Integer[] permissionIds, Integer[] menuIds);

    Result delete(Integer id);

    Result findPermissionIdsByRoleId(Integer roleId);

    Result findMenuIdsByRoleId(Integer roleId);
}
