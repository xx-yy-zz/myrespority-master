package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.Menu;
import com.itheima.pojo.User;
import com.itheima.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Reference
    UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @RequestMapping("/getUsername")
    public Result getUsername()throws Exception{
        try{
            org.springframework.security.core.userdetails.User user =
                    (org.springframework.security.core.userdetails.User)
                            SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return new Result(true, MessageConstant.GET_USERNAME_SUCCESS,user.getUsername());
        }catch (Exception e){
            return new Result(false, MessageConstant.GET_USERNAME_FAIL);
        }
    }


    @RequestMapping("/findPage")
    @PreAuthorize("hasAuthority('USER_QUERY')")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){

        PageResult pageResult=userService.pageQuery(queryPageBean.getCurrentPage(),
                queryPageBean.getPageSize(),
                queryPageBean.getQueryString());

        return pageResult;
    }

    @RequestMapping("/add")
    @PreAuthorize("hasAuthority('USER_ADD')")
    public Result add(@RequestBody User user, Integer[] roles){
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userService.add(user,roles);
            return new Result(true, MessageConstant.ADD_USER_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_USER_FAIL);
        }
    }

    @RequestMapping("/delete")
    @PreAuthorize("hasAuthority('USER_DELETE')")
    public Result delete(Integer id){
        try {
            userService.deleteById(id);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new Result(false,e.getMessage());
        }catch(Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.DELETE_USER_FAIL);
        }
        return new Result(true,MessageConstant.DELETE_USER_SUCCESS);
    }

    @RequestMapping("/findById")
    public Result findById(Integer id){
        try {
            User user=userService.findById(id);
            return new Result(true,MessageConstant.QUERY_USER_SUCCESS,user);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_USER_FAIL);
        }

    }

    @RequestMapping("/edit")
    @PreAuthorize("hasAuthority('USER_EDIT')")
    public Result edit(@RequestBody User user, Integer[] roles){
        try {
            userService.edit(user,roles);
            return new Result(true,MessageConstant.EDIT_USER_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_USER_FAIL);
        }
    }



    @RequestMapping("/queryId")
    public Result queryId(){
        org.springframework.security.core.userdetails.User user =
                (org.springframework.security.core.userdetails.User)
                        SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = user.getUsername();
        Integer userId= userService.queryIdByUsername(username);
        return  showMenuByUserId(userId);

    }




    @RequestMapping("showMenuByUserId")
    public Result showMenuByUserId(Integer userId){
        List<Map> menulist_qiantai = null;

        try {
            List<Menu> menuList = userService.showMenuByUserId(userId);

            //处理数据,下面由于我懒,所以没有再抽取一个方法
            menulist_qiantai = new ArrayList();
            //HashMap无序,血坑!!!
            Map menuMap1 = new LinkedHashMap<String,Object>();
        /*
                "path": "1",
                "title": "工作台",
                "icon":"fa-dashboard",
                "children": []
         */
            //第一个menu,数据库没有,因此手动封装
            menuMap1.put("path", "1");
            menuMap1.put("title", "工作台");
            menuMap1.put("icon", "fa-dashboard");
            menuMap1.put("children", new ArrayList<Map>());

            menulist_qiantai.add(menuMap1);
            //遍历menuList
            for (Menu menu : menuList) {
                //如果menu表的parentId字段为null
                if(null == menu.getParentMenuId()){
                    Map menuMap2 = new LinkedHashMap<String,Object>();
                    menuMap2.put("path", menu.getPath());
                    menuMap2.put("title", menu.getName());
                    menuMap2.put("icon", menu.getIcon());

                    List<Map> menulist_zi = new ArrayList();

                    //再遍历,如果子menu的parentId等于父menu的id
                    for (Menu menu1 : menuList) {
                        Map menu_zi = new LinkedHashMap<String,Object>();
                        if(menu.getId() == menu1.getParentMenuId()){
                            /*
                                "path": "/2-1",
                                 "title": "会员档案",
                                 "linkUrl":"member.html",
                                 "children":[]
                             */
                            menu_zi.put("path", menu1.getPath());
                            menu_zi.put("title", menu1.getName());
                            menu_zi.put("linkUrl", menu1.getLinkUrl());
                            menu_zi.put("children", new ArrayList());
                            menulist_zi.add(menu_zi);
                        }

                    }
                    menuMap2.put("children",menulist_zi);
                    menulist_qiantai.add(menuMap2);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_MENU_FAIL);
        }
        return new Result(true, MessageConstant.GET_MENU_SUCCESS, menulist_qiantai);
    }
}
