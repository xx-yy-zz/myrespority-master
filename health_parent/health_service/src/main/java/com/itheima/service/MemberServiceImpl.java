package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.MemberDao;
import com.itheima.pojo.Member;
import com.itheima.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Array;
import java.util.*;

@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService {
    @Autowired
    MemberDao memberDao;
    @Override
    public Member findByTelePhone(String telephone) {
        return memberDao.findByTelephone(telephone);
    }

    @Override
    public void add(Member member) {
        String password = member.getPassword();
        if (password!=null){
            //Md5加密
            String md5= MD5Utils.md5(password);
            member.setPassword(md5);
        }
        memberDao.add(member);
    }


    //根据月份进行会员数量的统计
    @Override
    public List<Integer> findMemberCountByMonth(List<String> months) {
        List<Integer> list=new ArrayList<>();
        for (String month : months) {//2019.05
            month=month+".31";
            Integer count = memberDao.findMemberCountBeforeDate(month);
            list.add(count);
        }
        return list;
    }

    /**
     * 会员年龄占比查询
     */
    @Override
    public List<Map<String,Object>> findMemberCountBySex() {
        List<Map<String,Object>> map=memberDao.findMemberCountBySex();

        return map;
    }

    /**
     * 会员性别占比查询
     */
    @Override
    public List<Map<String, Object>> findMemberCountByAge() {
        List<Map<String,Object>> map = memberDao.findMemberCountByAge();
        return map;
    }







}
