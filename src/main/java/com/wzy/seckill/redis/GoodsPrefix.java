package com.wzy.seckill.redis;

public class GoodsPrefix extends BasePrefix {

    public GoodsPrefix(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static GoodsPrefix goodsList = new GoodsPrefix(60, "gl");
    public static GoodsPrefix goodsDetail = new GoodsPrefix(60, "gdl");

}
