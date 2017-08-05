package cn.qgg.erp.biz.impl;

import cn.qgg.erp.biz.IStoreoperBiz;
import cn.qgg.erp.dao.IEmpDao;
import cn.qgg.erp.dao.IGoodsDao;
import cn.qgg.erp.dao.IStoreDao;
import cn.qgg.erp.dao.IStoreoperDao;
import cn.qgg.erp.entity.Storeoper;

import java.util.List;

/**
 * 仓库操作记录业务逻辑类
 * @author Administrator
 *
 */
public class StoreoperBiz extends BaseBiz<Storeoper> implements IStoreoperBiz {

	private IStoreoperDao storeoperDao;
	private IEmpDao empDao;
	private IGoodsDao goodsDao;
	private IStoreDao storeDao;

	@Override
	public List<Storeoper> findList(Storeoper t1, Storeoper t2, Object param) {
		List<Storeoper> list = super.findList(t1, t2, param);
		for (Storeoper storeoper : list) {
			storeoper.setEmpName(super.getName(storeoper.getEmpuuid(),empDao));
			storeoper.setGoodsName(super.getName(storeoper.getGoodsuuid(),goodsDao));
			storeoper.setStoreName(super.getName(storeoper.getStoreuuid(),storeDao));
		}
		return list;
	}

	@Override
	public List<Storeoper> findPageList(Storeoper t1, Storeoper t2, Object param, int firstResult, int maxResult) {
		List<Storeoper> list = super.findPageList(t1, t2, param, firstResult, maxResult);
		for (Storeoper storeoper : list) {
			storeoper.setEmpName(super.getName(storeoper.getEmpuuid(),empDao));
			storeoper.setGoodsName(super.getName(storeoper.getGoodsuuid(),goodsDao));
			storeoper.setStoreName(super.getName(storeoper.getStoreuuid(),storeDao));
		}
		return list;
	}

	public void setStoreoperDao(IStoreoperDao storeoperDao) {
		this.storeoperDao = storeoperDao;
		super.setBaseDAO(this.storeoperDao);
	}

	public void setEmpDao(IEmpDao empDao) {
		this.empDao = empDao;
	}

	public void setGoodsDao(IGoodsDao goodsDao) {
		this.goodsDao = goodsDao;
	}

	public void setStoreDao(IStoreDao storeDao) {
		this.storeDao = storeDao;
	}
}
