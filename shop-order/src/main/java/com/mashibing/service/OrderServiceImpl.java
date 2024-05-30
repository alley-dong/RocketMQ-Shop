package com.mashibing.service;

import com.mashibing.dao.ShopOrderMapper;
import com.mashibing.model.ShopOrder;
import com.google.gson.Gson;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.Date;


/**
 *@author 李瑾老师
 *
 *类说明：订单相关的服务
 */
@Service
@Transactional
public class OrderServiceImpl {
    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private ShopOrderMapper shopOrderMapper;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    private static final String SUCCESS = "success";
    private static final String FAILUER = "failure";
    @Autowired
    private RestTemplate restTemplate;

    @Value("${mq.order.topic}")
    private String topic;

    @Value("${mq.order.cancel.topic}")
    private String canceltopic;

    public long submitOrder(long userId,long goodsId, int goodsNumber,long couponId)throws Exception {
        ShopOrder shopOrder = new ShopOrder();
        shopOrder.setUserId(userId);
        shopOrder.setGoodsId(goodsId);
        shopOrder.setGoodsNumber(goodsNumber);
        shopOrder.setCouponId(couponId);
        shopOrder.setOrderStatus(0);
        shopOrder.setPayStatus(1);
        shopOrder.setShippingStatus(0);
        shopOrder.setAddTime(new Date());
        long orderid=0;
        shopOrderMapper.insert(shopOrder);

        if( shopOrder!=null && shopOrder.getOrderId()!=null) {
            orderid = shopOrder.getOrderId();
        }
        if(orderid<=0){
            return orderid;
        }
        //restFul调用商品系统（扣减库存）
        restUpdateGoods(shopOrder);

        //rest调用用户系统（处理优惠券）
        restUseCoupon(shopOrder);

        //把消息写入RocketMQ
       // MQShopOrder(shopOrder);

        return orderid;
    }

    public int restUpdateGoods(ShopOrder shopOrder) {
        String urlGoods = "http://127.0.0.1:8081/updateGoods";
        urlGoods = urlGoods + "?orderId=" + shopOrder.getOrderId() + "&goodsId=" + shopOrder.getGoodsId() + "&goodsNumber=" + shopOrder.getGoodsNumber();
        String str = restTemplate.getForEntity(urlGoods, String.class).getBody();
        restTemplate.delete(urlGoods);
        if (str.equals("success")) {
            return 1;
        }else{
            logger.error("RPC修改库存失败：[" + shopOrder.getOrderId() + "]");
            return 0;
        }
    }
    public int restUseCoupon(ShopOrder shopOrder) {
        String urlUser = "http://127.0.0.1:8082/useCoupon";
        urlUser = urlUser + "?orderId=" + shopOrder.getOrderId() + "&couponId=" + shopOrder.getCouponId();
        String str = restTemplate.getForEntity(urlUser, String.class).getBody();
        restTemplate.delete(urlUser);
        if (str.equals("success")) {
            return 1;
        }else{
            logger.error("RPC使用优惠券失败：[" + shopOrder.getOrderId() + "]");
            return 0;
        }

    }
    //生产者：向RocketMQ发送消息
    public int MQShopOrder(ShopOrder shopOrder) throws Exception {
        //TODO 使用Gson序列化
        Gson gson = new Gson();
        String txtMsg = gson.toJson(shopOrder);
        Message message = new Message(topic,"111",shopOrder.getOrderId().toString(),txtMsg.getBytes());

        SendResult sendResult = rocketMQTemplate.getProducer().send(message);
        rocketMQTemplate.getProducer().setRetryTimesWhenSendFailed(5);//重试5次  默认 RocketMq客户端 2次
        if(sendResult.getSendStatus() == SendStatus.SEND_OK){
            return  1;
        }else{
            logger.error("MQ发送消息失败：[" + shopOrder.getOrderId() + "]");
            return  -1;
        }
    }

    //生成限时订单
    public int MQDelayOrder(ShopOrder shopOrder) throws Exception {
        //TODO 使用Gson序列化
        Gson gson = new Gson();
        String txtMsg = gson.toJson(shopOrder);
        Message message = new Message("delayOrder","",shopOrder.getOrderId().toString(),txtMsg.getBytes());
        // delayTimeLevel：(1~18个等级)"1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h"
        message.setDelayTimeLevel(5);//1分钟不支付，就触发延时消息，就会把订单置为无效，还有回退。。。。
        SendResult sendResult = rocketMQTemplate.getProducer().send(message);
        if(sendResult.getSendStatus() == SendStatus.SEND_OK){
            return  1;
        }else{
            logger.error("MQ发送演示消息失败：[" + shopOrder.getOrderId() + "]");
            return  -1;
        }
    }


    //确认订单
    public int ConfirmOrder(long orderid) throws Exception {
        ShopOrder shopOrder=shopOrderMapper.selectByPrimaryKey(orderid);
        shopOrder.setOrderStatus(1);
        shopOrder.setPayStatus(2);
        if(  shopOrderMapper.updateByPrimaryKey(shopOrder)>0){
            return  1;
        }else{
            logger.error("确认订单失败：[" + orderid + "]");
            return  -1;
        }
    }


    //订单失败补偿消息
    public void CancelOrderMQ(ShopOrder shopOrder) throws Exception {
        if(shopOrder.getOrderId()<=0){
            return ;
        }
        Gson gson = new Gson();
        String txtMsg = gson.toJson(shopOrder);
        Message message = new Message(canceltopic,"",shopOrder.getOrderId().toString(),txtMsg.getBytes());
        SendResult sendResult= rocketMQTemplate.getProducer().send(message);
        if(sendResult.getSendStatus() != SendStatus.SEND_OK){
            logger.error("MQ订单失败补偿消息失败：[" + shopOrder.getOrderId() + "]");
        }
    }

    //超时订单处理
    public int dealDealyOrder(ShopOrder shopOrder) throws Exception {
        //再从数据库中查一下订单的实时状态  调用第三方的接口  -》 订单是否真的支付
        ShopOrder shopOrderReal= shopOrderMapper.selectByPrimaryKey(shopOrder.getOrderId());
        if(shopOrderReal ==null) return -1;
        if(shopOrderReal.getPayStatus()==2 ||shopOrderReal.getOrderStatus()==2
                ||shopOrderReal.getOrderStatus()==3
                ||shopOrderReal.getOrderStatus()==4){
            //logger.info("该订单已经付款：[" + shopOrderReal.getOrderId() + "]");
            return 1;
        }
        shopOrderReal.setOrderStatus(3);//订单状态（订单超时没有支付，支付失败） --3无效
        if(shopOrderMapper.updateByPrimaryKeySelective(shopOrderReal)>0){
            //logger.info("该订单已经超时，改为无效：[" + shopOrderReal.getOrderId() + "]");
            return  1;
        }else{
            logger.error("修改订单超时失败：[" + shopOrderReal.getOrderId() + "]");
            return  -1;
        }
    }
}
