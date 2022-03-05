package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.Menu;

import java.util.List;

public interface MenuDao {
    List<Menu> queryMenuByMenuId(List<Integer> menuId);

    Page<Menu> findPage(String queryString);

    void add(Menu menu);

    void edit(Menu menu);

    Long findCount(Integer id);

    void delete(Integer id);

    List<Menu> findAll();
}
