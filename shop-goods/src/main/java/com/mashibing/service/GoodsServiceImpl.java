package com.mashibing.service;

import com.mashibing.dao.ShopGoodsMapper;
import com.mashibing.dao.shopGoodsUniqueMapper;
import com.mashibing.model.ShopGoods;
import com.mashibing.model.shopGoodsUnique;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 *@author 李瑾老师
 *
 *类说明：订单相关的服务
 */
@Service
@Transactional
public class GoodsServiceImpl {

    private static final Logger logger = LoggerFactory.getLogger(GoodsServiceImpl.class);

	@Autowired
	private ShopGoodsMapper shopGoodsMapper;


    @Autowired
    private shopGoodsUniqueMapper shopGoodsUniqueMapper;

    public  int updateGoods(long orderId, long goodsId, int goodsNumber){
            //1、查询库存
            ShopGoods shopGoods =shopGoodsMapper.selectByPrimaryKey(goodsId);
            Integer goodnumber = shopGoods.getGoodsNumber()-goodsNumber;
            if(goodnumber<0){      //2、判断是否超卖
                return  -1; //这里就是超卖了，不再进行库存构建，不在进行订单的处理
            }
            //3、修改库存信息
            shopGoods.setGoodsNumber(goodnumber);

            if(shopGoodsMapper.updateByPrimaryKey(shopGoods)>=0){
                logger.info("修改库存成功：[" + orderId + "]");
                return 1;
            }else{
                logger.error("修改库存失败：[" + orderId + "]");
                return -1;
            }


    }











    //回退-扣减库存
    public  int CancelupdateGoods(long orderId, long goodsId, int goodsNumber){
        try {
            //去重表中有，才能证明是插入了，所以要回退
            shopGoodsUnique shopGoodsUnique = new shopGoodsUnique();
            shopGoodsUnique.setOrderId(orderId);
            shopGoodsUniqueMapper.insert(shopGoodsUnique);
        }catch (Exception e) {
            ShopGoods shopGoods =shopGoodsMapper.selectByPrimaryKey(goodsId);
            Integer goodnumber = shopGoods.getGoodsNumber()+goodsNumber;
            shopGoods.setGoodsNumber(goodnumber);
            if(shopGoodsMapper.updateByPrimaryKey(shopGoods)>=0){
                //logger.info("修改库存成功：[" + orderId + "]");
                return 1;
            }else{
                logger.error("回退库存失败：[" + orderId + "]");
                return -1;
            }
        }
      return 1;
    }


}
