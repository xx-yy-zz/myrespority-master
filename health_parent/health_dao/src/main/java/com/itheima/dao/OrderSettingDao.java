package com.itheima.dao;

import com.itheima.pojo.OrderSetting;


import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderSettingDao {
    long findCountByOrderDate(Date orderDate);

    void editNumberByOrderDate(OrderSetting orderSetting);

    void add(OrderSetting orderSetting);

    List<OrderSetting> getOrderSettingByMonth(Map<String, String> map);

    void editNumberByDate(OrderSetting orderSetting);

    OrderSetting findByOrderDate(Date date);

    void editReservationsByOrderDate(OrderSetting orderSetting);

    //删除相关的日期数据
    void delectOrdersettingByDate(Date date);

}
