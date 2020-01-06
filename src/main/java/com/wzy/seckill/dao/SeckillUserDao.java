package com.wzy.seckill.dao;

import com.wzy.seckill.domain.SeckillUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * 秒杀用户Dao
 * @author wzy
 * @version 1.0
 * @date 2019/12/19 10:55
 */
@Mapper
public interface SeckillUserDao {
    @Select("select * from seckill_user where id = #{id}")
    SeckillUser getById(@Param("id") long id);


    @Update("update seckill_user  set password = #{password} where id = #{id}")
    int updatePassword(SeckillUser user);

}
