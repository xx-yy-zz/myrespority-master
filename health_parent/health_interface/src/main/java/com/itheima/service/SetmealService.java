package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetmealService {
    void add(Setmeal setmeal, Integer[] checkgroupIds);

    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);

    List<Setmeal> findAll();

    Setmeal findById(Integer id);

    List<Map<String,Object>> findSetmealCount();
}
