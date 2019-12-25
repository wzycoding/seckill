package com.wzy.seckill.service;

import com.wzy.seckill.dao.GoodsDao;
import com.wzy.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * GoodsService
 * @author wzy
 * @version 1.0
 * @date 2019/12/19 21:16
 */
@Service
public class GoodsService {

    public List<GoodsVo> listGoodsVo() {
        return goodsDao.listGoodsVo();
    }

    @Resource
    private GoodsDao goodsDao;

}
