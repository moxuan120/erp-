package cn.qgg.erp.biz.impl;

import cn.qgg.erp.biz.IGoodsTypeBiz;
import cn.qgg.erp.dao.IGoodsDao;
import cn.qgg.erp.dao.IGoodsTypeDao;
import cn.qgg.erp.entity.Goods;
import cn.qgg.erp.entity.GoodsType;
import cn.qgg.erp.exception.ErpException;

import java.io.Serializable;
import java.util.List;

public class GoodsTypeBiz extends BaseBiz<GoodsType> implements IGoodsTypeBiz {
    private IGoodsTypeDao goodsTypeDao;
    private IGoodsDao goodsDao;

    @Override
    public void delete(Serializable id) {
        Goods goods = new Goods();
        GoodsType goodsType = new GoodsType();
        goodsType.setUuid((Long) id);
        goods.setGoodsType(goodsType);
        List<Goods> pageList = goodsDao.findPageList(goods, null, null, 0, 0);
        if(pageList!=null && pageList.size()>0) throw new ErpException("该类型下有商品");
        else super.delete(id);
    }

    public void setGoodsTypeDao(IGoodsTypeDao goodsTypeDao) {
        this.goodsTypeDao = goodsTypeDao;
        super.setBaseDAO(goodsTypeDao);
    }

    public void setGoodsDao(IGoodsDao goodsDao) {
        this.goodsDao = goodsDao;
    }
}
