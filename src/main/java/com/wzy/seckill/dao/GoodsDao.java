package com.wzy.seckill.dao;

import com.wzy.seckill.vo.GoodsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 商品dao
 * @author wzy
 * @version 1.0
 * @date 2019/12/19 21:17
 */
@Mapper
public interface GoodsDao {
    @Select("select g.*, sg.seckill_price, sg.stock_count, sg.start_date, sg.end_date from seckill_goods sg left join goods g on sg.goods_id = g.id")
    List<GoodsVo> listGoodsVo();
}
