package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.Role;

import com.itheima.service.RoleService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/role")
public class RoleController {

    @Reference
    private RoleService roleService ;
    @RequestMapping("/delete")
    @PreAuthorize("hasAuthority('ROLE_DELETE')")//权限校验
    public Result delete(Integer id){
        return roleService.delete(id);
    }

    @RequestMapping("/edit")
    @PreAuthorize("hasAuthority('ROLE_EDIT')")//权限校验
    public Result edit(@RequestBody Role role,Integer[]permissionIds,Integer[]menuIds ){
        return roleService.edit(role,permissionIds,menuIds);
    }

    @RequestMapping("/add")
    @PreAuthorize("hasAuthority('ROLE_ADD')")//权限校验
    public Result add(@RequestBody Role formData,Integer[]permissionIds,Integer[]menuIds){
        return roleService.add(formData,permissionIds,menuIds);
    }

    @RequestMapping("/findPage")
    @PreAuthorize("hasAuthority('ROLE_QUERY')")//权限校验
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        return roleService.findPage(queryPageBean);
    }
    @RequestMapping("/findAll")
    public Result findAll(){
        try {
            List<Role> roleList=roleService.findAll();
            return new Result(true, MessageConstant.QUERY_ROLE_SUCCESS,roleList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_ROLE_FAIL);
        }

    }

    //根据检查组查询其关联的检查项id
    @RequestMapping("/findroleIdsByUserId")
    public Result findroleIdsByUserId(Integer userId){
        try {
            Integer[] ids=roleService.findroleIdsByUserId(userId);
            return new Result(true,MessageConstant.QUERY_ROLE_SUCCESS,ids);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_ROLE_FAIL);
        }
    }

    @RequestMapping("/findPermissionIdsByRoleId")
    public Result findPermissionIdsByRoleId(Integer roleId){
        return roleService.findPermissionIdsByRoleId(roleId);
    }

    @RequestMapping("/findMenuIdsByRoleId")
    public Result findMenuIdsByRoleId(Integer roleId){
        return roleService.findMenuIdsByRoleId(roleId);
    }
}
