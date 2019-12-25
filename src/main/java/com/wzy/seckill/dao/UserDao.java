package com.wzy.seckill.dao;

import com.wzy.seckill.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author wzy
 * @version 1.0
 * @date 2019/12/16 10:55
 */
@Mapper
public interface UserDao {
    @Select("select * from user where id = #{id}")
    User getById(@Param("id") int id);

    @Insert("insert into user(id, name) value(#{user.id}, #{user.name})")
    int insert(@Param("user") User user);
}
