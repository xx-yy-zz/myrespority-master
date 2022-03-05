package com.itheima.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.List;

/*
套餐管理
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Reference
    SetmealService setmealService;

    @Autowired
    JedisPool jedisPool;

    @RequestMapping("/getSetmeal")
    public Result getSetmeal(){
        try{
            //从reids取出所有的套餐信息
            String setmealListStr = jedisPool.getResource().get("setmealListStr");
            if(setmealListStr == null){
                //从mysql数据库中取出所有的套餐信息
                List<Setmeal> list = setmealService.findAll();
                String setmealListJsonStr = JSON.toJSONString(list);
                jedisPool.getResource().set("setmealListStr",setmealListJsonStr);
                setmealListStr = setmealListJsonStr;
            }
            //将取出的套餐字符串转化成list
            ArrayList<Setmeal> setmeals = new ArrayList<>();
            List<Setmeal> setmeals1 = JSON.parseArray(setmealListStr, Setmeal.class);

            return new Result(true, MessageConstant.GET_SETMEAL_LIST_SUCCESS,setmeals1);
            //return new Result(true, MessageConstant.GET_SETMEAL_LIST_SUCCESS,list);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(true,MessageConstant.GET_SETMEAL_LIST_FAIL);
        }
    }

    //根据套餐id查询详情
    @RequestMapping("/findById")
    public Result findById(Integer id){
        //从reids中取出这个id的套餐
        String setmealDetailStr = jedisPool.getResource().get("setmealDetailStr" + id);
        if(setmealDetailStr ==null){
            //从mysql数据库中取出这个id的套餐
            Setmeal setmeal = setmealService.findById(id);
            String setmealDetailJsonStr = JSON.toJSONString(setmeal);
            jedisPool.getResource().set("setmealDetailStr"+id,setmealDetailJsonStr);
            setmealDetailStr = setmealDetailJsonStr;
        }
        //从reids中取出json字符串,重新转化为setmeal对象,传给前端
        Setmeal setmeal1 = JSON.parseObject(setmealDetailStr, Setmeal.class);
        return new Result(true,MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS,setmeal1);

    }
}
