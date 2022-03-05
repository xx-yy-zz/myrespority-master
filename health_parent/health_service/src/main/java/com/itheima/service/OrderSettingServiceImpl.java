package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.OrderSettingDao;
import com.itheima.pojo.OrderSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {
    //批量保存预约设置信息
    @Autowired
    OrderSettingDao orderSettingDao;
    @Override
    public void add(List<OrderSetting> data) {
        if (data!=null && data.size()>0){
            for (OrderSetting orderSetting : data) {
              //为了不让上传的文件重复
                //判断当前日期已经设置过了
                Date orderDate = orderSetting.getOrderDate();
                //根据日期统计
               long count =orderSettingDao.findCountByOrderDate(orderDate);

               if (count>0){
                   //已经设置过了,执行修改操作
                   orderSettingDao.editNumberByOrderDate(orderSetting);
               }else{
                   //没有设置过,执行插入操作
                   orderSettingDao.add(orderSetting);
               }


            }
        }

    }

    //根据年月查询对应的预约设置数据
    @Override
    public List<Map> getOrderSettingByMonth(String date) {
        String dateBegin=date+"-1";
        String dateEnd=date+"-31";
        Map<String, String> map = new HashMap<>();
        map.put("dateBegin",dateBegin);
        map.put("dateEnd",dateEnd);
        List<OrderSetting> list= orderSettingDao.getOrderSettingByMonth(map);
        ArrayList<Map> data = new ArrayList<>();
        if (list!=null && list.size()>0){
            for (OrderSetting orderSetting : list) {
                int dateNum=orderSetting.getOrderDate().getDate();
                int number = orderSetting.getNumber();
                int reservations = orderSetting.getReservations();
                HashMap<String, Integer> map1 = new HashMap<>();
                map1.put("date",dateNum);
                map1.put("number",number);
                map1.put("reservations",reservations);
                data.add(map1);
            }
        }
        return data;
    }

    @Override
    public void editNumberByDate(OrderSetting orderSetting) {
        long count = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
        if (count>0){
            orderSettingDao.editNumberByDate(orderSetting);
        }else{
            orderSettingDao.add(orderSetting);
        }


    }

    @Override
    public void delectOrdersettingByDate(Date data) {
        orderSettingDao.delectOrdersettingByDate(data);
    }


}
