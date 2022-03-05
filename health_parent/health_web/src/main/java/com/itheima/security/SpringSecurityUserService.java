package com.itheima.security;
import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;
import com.itheima.pojo.User;
import com.itheima.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class SpringSecurityUserService implements UserDetailsService {
    @Reference
    UserService userService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       User user=userService.findUserByusername(username);
       if (user==null){
           return null;
           //框架往外抛异常
       }
        List<GrantedAuthority> list=new ArrayList<GrantedAuthority>();
       //动态授权
        Set<Role> roles = user.getRoles();
        for (Role role : roles) {
            Set<Permission> permissions = role.getPermissions();
            for (Permission permission : permissions) {
                String keyword = permission.getKeyword();
                //为了给用户授予权限
                list.add(new SimpleGrantedAuthority(keyword));
            }
        }
        return new org.springframework.security.core.userdetails.User(username,user.getPassword(),list);


    }
}
