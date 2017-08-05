package cn.qgg.erp.biz.impl;
import cn.qgg.erp.biz.IStoredetailBiz;
import cn.qgg.erp.dao.IGoodsDao;
import cn.qgg.erp.dao.IStoreDao;
import cn.qgg.erp.dao.IStoredetailDao;
import cn.qgg.erp.entity.Storedetail;

import java.util.List;

/**
 * 仓库库存业务逻辑类
 * @author Administrator
 *
 */
public class StoredetailBiz extends BaseBiz<Storedetail> implements IStoredetailBiz {

	private IStoredetailDao storedetailDao;
	private IGoodsDao goodsDao;
	private IStoreDao storeDao;

    @Override
    public List<Storedetail> findList(Storedetail t1, Storedetail t2, Object param) {
        List<Storedetail> list = super.findList(t1, t2, param);
        for (Storedetail storedetail : list) {
            storedetail.setGoodsName(super.getName(storedetail.getGoodsuuid(),goodsDao));
            storedetail.setStoreName(super.getName(storedetail.getStoreuuid(),storeDao));
        }
        return list;
    }

    @Override
	public List<Storedetail> findPageList(Storedetail t1, Storedetail t2, Object param, int firstResult, int maxResult) {
		List<Storedetail> list = super.findPageList(t1, t2, param, firstResult, maxResult);
        for (Storedetail storedetail : list) {
            storedetail.setGoodsName(super.getName(storedetail.getGoodsuuid(),goodsDao));
            storedetail.setStoreName(super.getName(storedetail.getStoreuuid(),storeDao));
        }
        return list;
	}

	public void setStoredetailDao(IStoredetailDao storedetailDao) {
		this.storedetailDao = storedetailDao;
		super.setBaseDAO(this.storedetailDao);
	}

    public void setGoodsDao(IGoodsDao goodsDao) {
        this.goodsDao = goodsDao;
    }

    public void setStoreDao(IStoreDao storeDao) {
        this.storeDao = storeDao;
    }
}
