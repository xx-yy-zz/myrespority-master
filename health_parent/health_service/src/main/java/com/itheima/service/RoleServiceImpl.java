package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.itheima.constant.MessageConstant;
import com.itheima.dao.RoleDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.Role;
import com.qiniu.util.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service(interfaceClass = RoleService.class)
@Transactional
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleDao roleDao;

    @Override
    public List<Role> findAll() {
        return roleDao.findAll();
    }

    @Override
    public Integer[] findroleIdsByUserId(Integer userId) {
        return roleDao.findroleIdsByUserId(userId);
    }

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        Page<Role> page = roleDao.findPage(queryPageBean.getQueryString());
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public Result add(Role role,Integer[]permissionIds,Integer[]menuIds) {
        roleDao.add(role);
        addrole_permission(role.getId(),permissionIds);
        addrole_menu(role.getId(),menuIds);
        return new Result(true, MessageConstant.ADD_ROLE_SUCCESS);
    }

    @Override
    public Result edit(Role role, Integer[] permissionIds, Integer[] menuIds) {
        roleDao.edit(role);
        if (permissionIds != null & permissionIds.length > 0) {
            deleterole_permission(role.getId());
            addrole_permission(role.getId(), permissionIds);
        }
        if (menuIds != null & menuIds.length > 0){
            deleterole_menu(role.getId());
        addrole_menu(role.getId(), menuIds);
        }
        return new Result(true,MessageConstant.EDIT_ROLE_SUCCESS);
    }

    @Override
    public Result delete(Integer id) {
        deleterole_permission(id);
        deleterole_menu(id);
        roleDao.delete(id);
        return new Result(true,MessageConstant.DELETE_ROLE_SUCCESS);
    }

    @Override
    public Result findPermissionIdsByRoleId(Integer roleId) {
        List<Integer> list =roleDao.findPermissionIdsByRoleId(roleId);
        return new Result(true,MessageConstant.QUERY_ROLE_SUCCESS,list);
    }

    @Override
    public Result findMenuIdsByRoleId(Integer roleId) {
        List<Integer> list =roleDao.findMenuIdsByRoleId(roleId);
        return new Result(true,MessageConstant.QUERY_ROLE_SUCCESS,list);
    }

    public void addrole_permission(Integer id,Integer[]permissionIds){
        if(permissionIds!=null&&permissionIds.length>0) {
            for (Integer permissionid : permissionIds) {
                HashMap<String, Integer> map = new HashMap<String, Integer>();
                map.put("role_id", id);
                map.put("permission_id", permissionid);
                roleDao.addrole_permission(map);
            }
        }
    }

    public void addrole_menu(Integer id,Integer[]menuIds){
        if(menuIds!=null&&menuIds.length>0) {
            for (Integer menuid : menuIds) {
                HashMap<String, Integer> map = new HashMap<String, Integer>();
                map.put("role_id", id);
                map.put("menu_id", menuid);
                roleDao.addrole_menu(map);
            }
        }
    }
    public void deleterole_permission(Integer id){
        roleDao.deleterole_permission(id);
    }
    public void deleterole_menu(Integer id){
        roleDao.deleterole_menu(id);
    }

}
