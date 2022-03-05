package com.itheima.service;

import com.itheima.pojo.Member;

import java.util.List;
import java.util.Map;

public interface MemberService {
    Member findByTelePhone(String telephone);

    void add(Member member);

    List<Integer> findMemberCountByMonth(List<String> months);

    List<Map<String,Object>> findMemberCountBySex();

    List<Map<String,Object>> findMemberCountByAge();
}
