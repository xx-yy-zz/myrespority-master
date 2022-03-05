package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.Menu;
import com.itheima.service.MenuService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/menu")
public class MenuController {

    @Reference
    private MenuService menuService ;

    @RequestMapping("/findPage")
    @PreAuthorize("hasAuthority('MENU_QUERY')")//权限校验
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        return menuService.findPage(queryPageBean) ;
    }

    @RequestMapping("/add")
    @PreAuthorize("hasAuthority('MENU_ADD')")//权限校验
    public Result add(@RequestBody Menu menu){

        return menuService.add(menu);
    }

    @RequestMapping("/edit")
    @PreAuthorize("hasAuthority('MENU_EDIT')")//权限校验
    public Result edit(@RequestBody Menu menu){
        return menuService.edit(menu);
    }

    @RequestMapping("/delete")
    @PreAuthorize("hasAuthority('MENU_DELETE')")//权限校验
    public Result delete(Integer id){
        try{
        menuService.delete(id);
        return new Result(true, MessageConstant.DELETE_MENU_SUCCESS);
        }catch (RuntimeException e){
            return new Result(false,MessageConstant.DELETE_MENU_EXCEPTION);
        }catch (Exception e){
            return new Result(false,MessageConstant.DELETE_MENU_FAIL);
        }
    }

    @RequestMapping("/findAll")
    public Result findAll(){
        return menuService.findAll();
    }
}
