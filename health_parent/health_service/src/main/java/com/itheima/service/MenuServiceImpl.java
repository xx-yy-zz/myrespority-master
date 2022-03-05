package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.constant.MessageConstant;
import com.itheima.dao.MenuDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.awt.SystemColor.menu;

@Service(interfaceClass = MenuService.class)
@Transactional
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuDao menuDao ;

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        Page<Menu> page= menuDao.findPage(queryPageBean.getQueryString());
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public Result add(Menu menu) {
        try{
            menuDao.add(menu);
        }catch (Exception e){
            return new Result(false, MessageConstant.ADD_MENU_FAIL);
        }
        return new Result(true,MessageConstant.ADD_MENU_SUCCESS) ;
    }

    @Override
    public Result edit(Menu menu) {
        try {
            menuDao.edit(menu);
        }catch (Exception e){
            return new Result(false,MessageConstant.EDIT_MENU_FAIL);
        }
        return new Result(true,MessageConstant.EDIT_MENU_SUCCESS);
    }

    @Override
    public void delete(Integer id) {
        Long cont = menuDao.findCount(id);
        if(cont>0){
            throw new RuntimeException();
        }
       menuDao.delete(id);
    }

    @Override
    public Result findAll() {
        List<Menu> menuList = menuDao.findAll();
        return new Result(true,MessageConstant.GET_MENU_SUCCESS,menuList);
    }


}
