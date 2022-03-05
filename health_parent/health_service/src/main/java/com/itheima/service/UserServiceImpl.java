package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.constant.MessageConstant;
import com.itheima.dao.MenuDao;
import com.itheima.dao.PermissionDao;
import com.itheima.dao.RoleDao;
import com.itheima.dao.UserDao;
import com.itheima.entity.PageResult;
import com.itheima.pojo.Menu;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;
import com.itheima.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service(interfaceClass = UserService.class)
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;
    @Autowired
    RoleDao roleDao;
    @Autowired
    MenuDao menuDao;

    @Autowired
    PermissionDao permissionDao;
    //根据用户名查询用户信息,同时要查询用户关联的角色,角色关联的权限分别来查
    @Override
    public User findUserByusername(String username) {
        //1.只查询用户表信息
        User user=userDao.findByusername(username);
        if (user==null){
            return null;
        }
        //根据用户id查询关联的角色
        Integer userId = user.getId();
        Set<Role> roles=roleDao.findByuserId(userId);
        if (roles!=null && roles.size()>0){
            user.setRoles(roles);
            for (Role role : roles) {
                Integer roleId= role.getId();
                Set<Permission> permissions=permissionDao.findByRoleId(roleId);
                role.setPermissions(permissions);
            }
        }
        return user;
    }

    @Override
    public List<Menu> showMenuByUserId(Integer id) {
        //1.通过user_id查t_user_role表得role_id
        Integer roleId = userDao.queryRoleIdByUserId(id);
        //2.通过role_id查t_role_menu表得menu_id集合
        List<Integer> menuIds = roleDao.queryMenuIdsByRoleId(roleId);
        //3.通过menuIds批量查t_menu表得menu的所有数据,问题在于封装数据的格式
        List<Menu> menuList = menuDao.queryMenuByMenuId(menuIds);
        //4.controller层处理数据,再返json到前端
        return menuList;
    }

    @Override
    public PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage,pageSize);
        Page<User> page=userDao.pageQuery(queryString);

        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public void add(User user, Integer[] roles) {
        userDao.add(user);
        Integer userId = user.getId();
        setUserAndRole(userId,roles);
    }

    @Override
    public void deleteById(Integer id) {
        long count = userDao.findCountByUserId(id);
        if (count>0){
            throw new RuntimeException(MessageConstant.USER_IS_ASSOCATION);
        }
        userDao.deleteById(id);
    }

    @Override
    public User findById(Integer id) {

        return userDao.findById(id);
    }

    @Override
    public void edit(User user, Integer[] roles) {
        //编辑检查组的基本信息.重新设置检查组和检查项的关联关系
        //修改基本信息
        userDao.edit(user);

        //删除检查组和检查项的关联关系

        userDao.deleteAssociationByUserId(user.getId());


        //重新设置关联关系

        this.setUserAndRole(user.getId(),roles);
    }

    @Override
    public Integer queryIdByUsername(String username) {
        return userDao.queryIdByUsername(username);
    }

    public void setUserAndRole(Integer userId,Integer[] roles){
        if (roles!=null && roles.length>0){
            Map<String, Integer> map = new HashMap<>();
            for (Integer roleId : roles) {
                map.put("user_id",userId);
                map.put("role_id",roleId);
                userDao.setUserAndRole(map);
            }
        }
    }
}
