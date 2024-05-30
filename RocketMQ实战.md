# RocketMQ实战

## 项目改造前

一个电商网站的精简版

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1689144907053/fff112f502b74571a67c36fa8d5e1372.png)

### Maven依赖

* SpringBoot
* Mybatis

### 业务介绍

电商网站购物场景中的【下单】业务

1. 用户请求订单系统下单
2. 订单系统通过RPC调用订单服务下单
3. 订单服务调用调用库存服务，扣减库存
4. 订单服务调用优惠券服务，扣减优惠券
5. 订单支付完成后确认订单

## 加入RocketMQ实现异步与解耦功能

* 加入RocketMQ实现异步与解耦功能
* 利用RocketMQ的延时消息实现限时订单功能
* 消息重复消费的幂等性处理方案

### Maven依赖

* SpringBoot
* Mybatis
* rocketmq-spring-boot-starter

### **分布式系统宕机问题**

整个系统是分布式部署，有订单系统、商品系统、会员系统。三个系统通过RPC调用完成整个下单流程。RPC调用会导致下单中各系统耦合在一起，假如会员系统宕机，会导致下单流程的不可用。

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1689144907053/cb87b77a67b34f90817556c8b13653e4.png)

**如何异步解耦：**

利用RocketMQ，订单系统在下单后，作为生产者把“下单消息”写入MQ，商品系统与会员系统作为消费者消费MQ中的“下单消息”。这样可以达到异步解耦的目的，只要订单系统正常，对于用户来说下单业务都可以正常进行。

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1689144907053/3724371509434f02b78552713948e024.png)

## **消息重复消费的幂等性处理方案**

因为RocketMQ没有办法确保消息不重复。

1、网络导致的问题，消息成功发送到MQ中，但MQ因网络原因未能成功返回，导致重试机制重试机制重复发送到MQ。

2、消费者成功消费完消息，未返回consume_commit时，系统重启|系统宕机，
MQ重新发送消息到同消息组其他消费者机器，导致消息重复。

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1689144907053/d0d979b3eb9c4c66b6f06e49989b561e.png)

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1689144907053/74e05dd542294ac7a0274b0a58de8c71.png)

#### ***项目中使用去重表解决幂等性问题***

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1689144907053/f374baf459b14ce4bc678146b4ba6008.png)

每次在操作库存时，往专属的去重表中插入一条记录，因为order_id是唯一的，如果有两次，那么第二次肯定插入不了。所以这里就直接返回，不再修改库存。

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1689144907053/36f78ee687a349f883f4a9c8ca7f025c.png)

## 利用RocketMQ的延时消息实现限时订单功能

### **概念介绍**

**延时消息：** Producer 将消息发送到消息队列RocketMQ 服务端，但并不期望这条消息立马投递，而是延迟一定时间后才投递到Consumer 进行消费，该消息即延时消息。

### **适用场景**

消息生产和消费有时间窗口要求：比如在电商交易中超时未支付关闭订单的场景，在订单创建时会发送一条延时消息。这条消息将会在 30 分钟以后投递给消费者，消费者收到此消息后需要判断对应的订单是否已完成支付。如支付未完成，则关闭订单。如已完成支付则忽略。

### **使用方式**

发送延时消息时需要设定一个延时时间长度，消息将从当前发送时间点开始延迟固定时间之后才开始投递。

延迟消息是根据延迟队列的level来的，延迟队列默认是

msg.setDelayTimeLevel(3)代表延迟10秒

"1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h"

![](file:///C:\Users\lijin\AppData\Local\Temp\ksohtml7124\wps8.jpg)![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1689144907053/d7b47a58b8204edc8d9697498781b546.png)

是这18个等级（秒（s）、分（m）、小时（h）），level为1，表示延迟1秒后消费，level为5表示延迟1分钟后消费，level为18表示延迟2个小时消费。生产消息跟普通的生产消息类似，只需要在消息上设置延迟队列的level即可。消费消息跟普通的消费消息一致。

### 实战演示

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/5983/1689144907053/e1815fab081d4c5a8955ff2a7eecf732.png)
