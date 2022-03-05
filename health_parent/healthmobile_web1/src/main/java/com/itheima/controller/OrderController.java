package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.aliyuncs.exceptions.ClientException;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Order;
import com.itheima.service.OrderService;
import com.itheima.utils.SMSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Reference
    OrderService orderService;
    @Autowired
    JedisPool jedisPool;
    @RequestMapping("/submit")
    public Result submitOrder(@RequestBody Map map){
        /*
        从Redis中获取缓存的验证码,key为手机号+
        RedisConstant.SENDTYPE_ORDER
         */
        String telephone = (String) map.get("telephone");
        String codeInRedis = jedisPool.getResource().get(
                telephone + RedisMessageConstant.SENDTYPE_ORDER
        );
        String validateCode=(String)map.get("validateCode");
        //检验手机验证码
        if (codeInRedis==null || !codeInRedis.equals(validateCode)){
            //验证码输入错误
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        //调用体检预约服务
        Result result=null;
        try{
            map.put("orderType", Order.ORDERTYPE_WEIXIN);
            result=orderService.order(map);
        }catch(Exception e){
            e.printStackTrace();
            //预约失败
            return result;
        }
        if (result.isFlag()){
            //预约成功,发送短信通知
            String orderDate = (String) map.get("orderDate");
            try {
                SMSUtils.sendShortMessage(SMSUtils.ORDER_NOTICE, telephone, orderDate);
            }catch(ClientException e){
                e.printStackTrace();
            }

        }
        return result;

    }
   @RequestMapping("/findById")
    public Result findById(Integer id){
        try {
            Result result=orderService.findById(id);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ORDER_FAIL);
        }


    }
}
