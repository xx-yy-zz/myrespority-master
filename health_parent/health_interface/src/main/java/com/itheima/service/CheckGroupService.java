package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.pojo.CheckGroup;

import java.util.List;

public interface CheckGroupService {

    //新增检查组
    void add(CheckGroup checkGroup, Integer[] checkitemIds);

    PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString);

    CheckGroup findById(Integer id);

    void edit(CheckGroup checkGroup, Integer[] checkitemIds);


    List<CheckGroup> findAll();

    void deleteById(Integer id);
}
