package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckItem;
import com.itheima.pojo.Permission;
import com.itheima.service.PermissionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/permission")
public class PermissionController {

    @Reference
    PermissionService permissionService;

    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult = permissionService.pageQuery(
                queryPageBean.getCurrentPage(),
                queryPageBean.getPageSize(),
                queryPageBean.getQueryString()
        );
        return pageResult;
    }

    @RequestMapping("/add")
    public Result add(@RequestBody Permission permission){
        try{
            permissionService.add(permission);
        }catch(Exception e){
            return new Result(false, MessageConstant.ADD_PERMISSION_FAIL);
        }
        return  new Result(true,MessageConstant.ADD_PERMISSION_SUCCESS);

    }

    @RequestMapping("/delete")
    public Result delete(Integer id){
        try {
            permissionService.deleteById(id);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new Result(false,e.getMessage());
        }catch(Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.DELETE_PERMISSION_FAIL);
        }
        return new Result(true,MessageConstant.DELETE_PERMISSION_SUCCESS);
    }

    @RequestMapping("/findById")
    public Result findById(Integer id){
        try{
            Permission permission = permissionService.findById(id);
            return  new Result(true, MessageConstant.QUERY_PERMISSION_SUCCESS,permission);
        }catch (Exception e){
            e.printStackTrace();
            return  new Result(false, MessageConstant.QUERY_PERMISSION_FAIL);
        }

    }

    @RequestMapping("/edit")
    public Result edit(@RequestBody Permission permission){
        try {
            permissionService.edit(permission);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_PERMISSION_FAIL);
        }
        return new Result(true,MessageConstant.EDIT_PERMISSION_SUCCESS);
    }

    @RequestMapping("/findAll")
    public Result findAll(){
        try {
            List<Permission> permissionList=permissionService.findAll();
            return new Result(true,MessageConstant.QUERY_PERMISSION_SUCCESS,permissionList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_PERMISSION_FAIL);
        }

    }




}
