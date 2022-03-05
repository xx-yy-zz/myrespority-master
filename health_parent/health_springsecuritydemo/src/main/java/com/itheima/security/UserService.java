package com.itheima.security;

import com.itheima.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserService implements UserDetailsService {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    //模拟数据库中的用户数据
    public  static  Map<String, com.itheima.pojo.User> map = new HashMap<>();
    public void initData(){
        com.itheima.pojo.User user1 = new com.itheima.pojo.User();
        user1.setUsername("admin");
        user1.setPassword(passwordEncoder.encode("admin"));

        com.itheima.pojo.User user2 = new com.itheima.pojo.User();
        user2.setUsername("xiaoming");
        user2.setPassword(passwordEncoder.encode("1234"));

        map.put(user1.getUsername(),user1);
        map.put(user2.getUsername(),user2);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        initData();
        User user = map.get(username);
        //没有查到
        if (user==null){
            return null;
        }
        //为用户授权,后期查询数据库
        String passwordIndb = user.getPassword();

        List<GrantedAuthority> list = new ArrayList<>();

        if (username.equals("admin")){
            list.add(new SimpleGrantedAuthority("add"));//权限
        }
        //授权，后期需要改为查询数据库动态获得用户拥有的权限和角色

        list.add(new SimpleGrantedAuthority("delete"));//授全
        list.add(new SimpleGrantedAuthority("ROLE_ADMIN"));//授予角色



        //查到用户信息
        org.springframework.security.core.userdetails.User user1 = new org.springframework.security.core.userdetails.User(username, passwordIndb, list);


        return user1;
    }
}
