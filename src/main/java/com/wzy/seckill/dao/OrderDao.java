package com.wzy.seckill.dao;

import com.wzy.seckill.domain.OrderInfo;
import com.wzy.seckill.domain.SeckillOrder;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;

@Mapper
public interface OrderDao {

    @Select("select * from seckill_order where user_id = #{userId} and goods_id = #{goodsId}")
    SeckillOrder getSeckillOrderByUserIdAndGoodsId(long userId, long goodsId);

    @Insert("insert into order_info(user_id, goods_id, delivery_addr_id, goods_name, goods_count, " +
            "goods_price, order_channel, status, create_date, pay_date) " +
            "values(#{userId}, #{goodsId}, #{deliveryAddrId}, #{goodsName}, #{goodsCount}, " +
            "#{goodsPrice}, #{orderChannel}, #{status}, #{createDate}, #{payDate})")
    @SelectKey(keyProperty = "id", keyColumn = "id", resultType = Long.class, before = false, statement= "select last_insert_id()")
    long insert(OrderInfo orderInfo);

    @Insert(" insert into seckill_order(user_id, goods_id, order_id)" +
            " values(#{userId}, #{goodsId}, #{orderId})")
    int insertSeckillOrder(SeckillOrder seckillOrder);
}
