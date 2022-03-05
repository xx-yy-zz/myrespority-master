package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.constant.MessageConstant;
import com.itheima.dao.CheckGroupDao;
import com.itheima.entity.PageResult;
import com.itheima.pojo.CheckGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {

    @Autowired
    CheckGroupDao checkGroupDao;

    @Override
    public void add(CheckGroup checkGroup, Integer[] checkitemIds) {
        checkGroupDao.add(checkGroup);
        Integer checkGroupId = checkGroup.getId();
        setCheckGroupAndCheckItem(checkGroupId,checkitemIds);
    }


    @Override
    public PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage,pageSize);
        Page<CheckGroup> page=checkGroupDao.pageQuery(queryString);

        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public CheckGroup findById(Integer id) {

        return checkGroupDao.findById(id);
    }

    @Override
    public void edit(CheckGroup checkGroup, Integer[] checkitemIds) {
        //编辑检查组的基本信息.重新设置检查组和检查项的关联关系
        //修改基本信息
        checkGroupDao.edit(checkGroup);

        //删除检查组和检查项的关联关系

        checkGroupDao.deleteAssociationByCheckGroupId(checkGroup.getId());


        //重新设置关联关系

        this.setCheckGroupAndCheckItem(checkGroup.getId(),checkitemIds);
    }

    @Override
    public List<CheckGroup> findAll() {

        return checkGroupDao.findAll();
    }

    @Override
    public void deleteById(Integer id) {
        long count = checkGroupDao.findCountByCheckgroupId(id);
        if (count>0){
            throw new RuntimeException(MessageConstant.CHECKGROUP_IS_ASSOCATION);
        }
        checkGroupDao.deleteById(id);
    }

    //设置检查组和检查项的关联关系
    public void setCheckGroupAndCheckItem(Integer checkGroupId,Integer[] checkitemIds){
        if (checkitemIds!=null && checkitemIds.length>0){
            Map<String, Integer> map = new HashMap<>();
            for (Integer checkitemId : checkitemIds) {
                map.put("checkgroup_id",checkGroupId);
                map.put("checkitem_id",checkitemId);

                checkGroupDao.setCheckGroupAndCheckItem(map);
            }
        }
    }
}
