package com.itheima.jobs;
import com.alibaba.dubbo.config.annotation.Reference;

import com.itheima.service.OrderSettingService;




import java.util.Date;
////3、定时器已经基本满足了基本业务的开发，
// 但是，由于定时器的执行优先于注入，因此我们不能通过@Resource注入service。

public class ClearOrderSetting  {
    @Reference
    OrderSettingService orderSettingService;

public  void  clearOrderSettingBefer(){
  orderSettingService.delectOrdersettingByDate(new Date());
}
}
