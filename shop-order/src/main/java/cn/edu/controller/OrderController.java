package cn.edu.controller;

import cn.edu.model.ShopOrder;
import cn.edu.service.OrderServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

/**
 * @author King老师
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
            //确认订单
            ShopOrder shopOrder = new ShopOrder();
            shopOrder.setUserId(userId);
            shopOrder.setGoodsId(goodsId);
            shopOrder.setGoodsNumber(goodsNumber);
            shopOrder.setCouponId(couponId);
            shopOrder.setOrderStatus(0);
            shopOrder.setPayStatus(1);
            shopOrder.setShippingStatus(0);
            shopOrder.setAddTime(new Date());
            orderid =orderService.submitOrder(shopOrder);
        } catch (Exception e) {
            e.printStackTrace();
            return FAILUER;
        }
        if(orderid>0){
            return SUCCESS+":"+orderid;
        }else{
            logger.error("提交订单失败：[" + orderid + "]");
            return FAILUER;
        }
    }
}
