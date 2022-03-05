package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.Menu;

public interface MenuService {

    PageResult findPage(QueryPageBean queryPageBean);

    Result add(Menu menu);

    Result edit(Menu menu);

    void delete(Integer id);

    Result findAll();
}
