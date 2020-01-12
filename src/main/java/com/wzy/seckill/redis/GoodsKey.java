package com.wzy.seckill.redis;

public class GoodsKey extends BasePrefix {

    public GoodsKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }
    public GoodsKey(String prefix) {super(prefix);}

    public static GoodsKey goodsList = new GoodsKey(60, "gl");
    public static GoodsKey goodsDetail = new GoodsKey(60, "gdl");
    public static GoodsKey getSeckillGoodsStock = new GoodsKey(0, "gs");

    public static GoodsKey getGoodsOver = new GoodsKey("gover");

}
