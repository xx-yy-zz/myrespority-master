package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckGroup;

import java.util.List;
import java.util.Map;

public interface CheckGroupDao {

    void add(CheckGroup checkGroup);

    void setCheckGroupAndCheckItem(Map<String, Integer> map);

    Page<CheckGroup> pageQuery(String queryString);

    CheckGroup findById(Integer id);

    Integer[] findcheckitemIdsByCheckGroupId(Integer checkGroupId);

    void edit(CheckGroup checkGroup);

    void deleteAssociationByCheckGroupId(Integer id);

    List<CheckGroup> findAll();

    long findCountByCheckgroupId(Integer id);

    void deleteById(Integer id);
}
