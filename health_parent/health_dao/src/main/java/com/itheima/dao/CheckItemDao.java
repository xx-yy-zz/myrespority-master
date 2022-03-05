package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckItem;

import java.util.List;

public interface CheckItemDao {

    void add(CheckItem checkItem);

    Page<CheckItem> pageQuery(String queryString);

    void deleteById(Integer id);

    long findCountByCheckItemId(Integer checkItemId);

    void edit(CheckItem checkItem);

    CheckItem findById(Integer id);

    List<CheckItem> findAll();

    Integer[] findcheckitemIdsByCheckGroupId(Integer checkGroupId);
}
