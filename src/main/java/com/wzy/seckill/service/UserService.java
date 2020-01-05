package com.wzy.seckill.service;

import com.wzy.seckill.dao.UserDao;
import com.wzy.seckill.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author wzy
 * @version 1.0
 * @date 2019/12/16 10:57
 */
@Service
public class UserService {
    @Resource
    private UserDao userDao;

    public User getById(int id){
        return userDao.getById(id);
    }

    @Transactional
    public boolean tx() {
        User user1 = new User();
        user1.setId(2);
        user1.setName("yuanyuan");
        userDao.insert(user1);

        //插入一个重复的id，测试回滚
        User user2 = new User();
        user2.setId(1);
        user2.setName("zhutou");
        userDao.insert(user2);
        return true;
    }
}
