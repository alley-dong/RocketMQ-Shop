package com.mashibing.controller;

import com.mashibing.model.ShopOrder;
import com.mashibing.service.OrderServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStreamWriter;
import java.util.Date;

/**
 * @author 李瑾老师
 * 类说明：订单相关的类
 */
@RestController
public class OrderController {
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    private static final String SUCCESS = "success";
    private static final String FAILUER = "failure";

    @Autowired
    private OrderServiceImpl orderService;
    //  http://localhost:8080/submitOrder?userId=1&goodsId=13&goodsNumber=1&couponId=1
    //提交订单
    @RequestMapping("/submitOrder")
    public String submitOrder(@RequestParam("userId")long userId,@RequestParam("goodsId")long goodsId,@RequestParam("goodsNumber")int goodsNumber,@RequestParam("couponId")long couponId){
        long orderid ;
        try {
            //check() 略过
            orderid =orderService.submitOrder(userId,goodsId,goodsNumber,couponId);
        } catch (Exception e) {
            logger.error("提交订单失败");
            e.printStackTrace();
            return FAILUER;
        }
        return SUCCESS+":"+orderid;
    }
}
