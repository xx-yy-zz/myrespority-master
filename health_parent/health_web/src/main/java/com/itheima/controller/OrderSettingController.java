package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderSettingService;
import com.itheima.utils.POIUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {

    @Reference
    OrderSettingService orderSettingService;

    @RequestMapping("/upload")
    public Result upload(@RequestParam("excelFile") MultipartFile excelFile){
        try {
            //先把文件里的数据读出来
           List<String[]> list= POIUtils.readExcel(excelFile);
           if (list!=null && list.size()>0){
               List<OrderSetting> data=new ArrayList<>();
               for (String[] row : list) {
                   String orderDate=row[0];//预约日期
                   String number=row[1];//可预约数量
                   OrderSetting orderSetting = new OrderSetting(new Date(orderDate), Integer.parseInt(number));
                   data.add(orderSetting);
               }
               //
               orderSettingService.add(data);
           }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true, MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
        return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
    }

    @RequestMapping("/getOrderSettingByMonth")
    public Result getOrderSettingByMonth(String date){
        try {
            List<Map> list = orderSettingService.getOrderSettingByMonth(date);
            return new Result(true,MessageConstant.GET_ORDERSETTING_SUCCESS,list);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_ORDERSETTING_FAIL);
        }

    }
    @RequestMapping("/editNumberByDate")
    @PreAuthorize("hasAuthority('ORDERSETTING')")//权限校验
    public Result editNumberByDate(@RequestBody OrderSetting orderSetting){
            try {
                orderSettingService.editNumberByDate(orderSetting);
                return new Result(true,MessageConstant.ORDERSETTING_SUCCESS);
            }catch(Exception e){
                e.printStackTrace();
                return new Result(false,MessageConstant.ORDERSETTING_FAIL);

            }
    }

}
