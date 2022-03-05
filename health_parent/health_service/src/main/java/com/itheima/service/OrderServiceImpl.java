package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.constant.MessageConstant;
import com.itheima.dao.MemberDao;
import com.itheima.dao.OrderDao;
import com.itheima.dao.OrderSettingDao;
import com.itheima.entity.Result;

import com.itheima.pojo.Member;
import com.itheima.pojo.Order;
import com.itheima.pojo.OrderSetting;
import com.itheima.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderSettingDao orderSettingDao;
    @Autowired
    MemberDao memberDao;
    @Autowired
    OrderDao orderDao;
    @Override
    /*
    体检预约方法处理逻辑比较复杂，需要进行如下业务处理：

1、检查用户所选择的预约日期是否已经提前进行了预约设置，如果没有设置则无法进行预约

2、检查用户所选择的预约日期是否已经约满，如果已经约满则无法预约

3、检查用户是否重复预约（同一个用户在同一天预约了同一个套餐），如果是重复预约则无法完成再次预约

4、检查当前用户是否为会员，如果是会员则直接完成预约，如果不是会员则自动完成注册并进行预约

5、预约成功，更新当日的已预约人数

     */

    //体检预约
    public Result order(Map map) throws Exception {
        //检查当前日期是否进行了预约设置
        String orderDate = (String) map.get("orderDate");
      try {
          OrderSetting orderSetting = orderSettingDao.findByOrderDate(DateUtils.parseString2Date(orderDate));
          if (orderSetting == null) {
              //1.没有进行预约设置,不能
              return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
          }
      //    2、检查用户所选择的预约日期是否已经约满，如果已经约满则无法预约
          int number = orderSetting.getNumber();
          int reservations = orderSetting.getReservations();
          if (reservations>=number){
              return new Result(false,MessageConstant.ORDER_FULL);
          }
          //3、检查用户是否重复预约（同一个用户在同一天预约了同一个套餐），
          // 如果是重复预约则无法完成再次预约

          String setmeal_Id = (String) map.get("setmealId");
          int setmealId = Integer.parseInt(setmeal_Id);
          String telephone = (String) map.get("telephone");
          Order order=null;
          //4根据手机号查询会员
          Member member = memberDao.findByTelephone(telephone);
          if (member!=null){
              Integer memberId = member.getId();
              order=new Order(memberId,DateUtils.parseString2Date(orderDate),null,null,setmealId);
              List<Order> list = orderDao.findByCondition(order);
              if (list!=null && list.size()>0){
                  return new Result(false,MessageConstant.HAS_ORDERED);
              }
          }else{
              //不是会员自动注册
              member=new Member();
              member.setName((String) map.get("name"));
              member.setPhoneNumber(telephone);
              member.setIdCard((String) map.get("idCard"));
              member.setSex((String) map.get("sex"));
              member.setRegTime(new Date());
              memberDao.add(member);
          }
          //可以进行预约了
          Order o=new Order(member.getId(),
                  DateUtils.parseString2Date(orderDate),
                  (String) map.get("orderType"),
                  Order.ORDERSTATUS_NO,Integer.parseInt((String) map.get("setmealId")));
          orderDao.add(o);
          //更新预约数据
          orderSetting.setReservations(orderSetting.getReservations()+1);
          orderSettingDao.editReservationsByOrderDate(orderSetting);
          return new Result(true,MessageConstant.ORDER_SUCCESS,o);
      }catch(Exception e){
          e.printStackTrace();
          return new Result(true,MessageConstant.ORDER_FAIL);
      }

    }

    @Override
    public Result findById(Integer id) throws Exception{
        Map map = orderDao.findById4Detail(id);
        if (map!=null){
            //处理日期
            Date orderDate = (Date) map.get("orderDate");
            map.put("orderDate",DateUtils.parseDate2String(orderDate));
            return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,map);
        }
        return new Result(false,MessageConstant.QUERY_ORDER_FAIL);
    }
}
