package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetmealDao {
    void add(Setmeal setmeal);

    void setSetmealAndCheckGroups(Map<String, Integer> map);

    Page<Setmeal> pageQuery(String queryString);

    List<Setmeal> findAll();

    Setmeal findById(Integer id);

    List<Map<String,Object>> findSetmealCount();
}
