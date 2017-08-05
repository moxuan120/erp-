package cn.qgg.erp.biz.impl;

import java.util.Date;
import java.util.List;

import cn.qgg.erp.biz.IInventoryBiz;
import cn.qgg.erp.dao.IEmpDao;
import cn.qgg.erp.dao.IGoodsDao;
import cn.qgg.erp.dao.IInventoryDao;
import cn.qgg.erp.dao.IStoreDao;
import cn.qgg.erp.dao.IStoredetailDao;
import cn.qgg.erp.dao.IStoreoperDao;
import cn.qgg.erp.entity.Inventory;
import cn.qgg.erp.entity.Storedetail;
import cn.qgg.erp.entity.Storeoper;
import cn.qgg.erp.exception.ErpException;

/**
 * 盘盈盘亏业务逻辑类
 * 
 * @author Administrator
 *
 */
public class InventoryBiz extends BaseBiz<Inventory> implements IInventoryBiz {

	private IInventoryDao inventoryDao;
	private IEmpDao empDao;
	private IStoreDao storeDao;
	private IGoodsDao goodsDao;
	private IStoredetailDao storedetailDao;
	private IStoreoperDao storeoperDao;

	@Override
	public List<Inventory> findPageList(Inventory t1, Inventory t2, Object param, int firstResult, int maxResult) {
		List<Inventory> list = inventoryDao.findPageList(t1, t2, param, firstResult, maxResult);
		// 补全名字
		for (Inventory inventory : list) {
			inventory.setCreaterName(getName(inventory.getCreater(), empDao));
			inventory.setCheckerName(getName(inventory.getChecker(), empDao));
			inventory.setStoreName(getName(inventory.getStoreuuid(), storeDao));
			inventory.setGoodsName(getName(inventory.getGoodsuuid(), goodsDao));
		}

		return list;
	}

	public void doCheck(Inventory t1, Long uuid) {
		// TODO Auto-generated method stub
		Inventory inventory = inventoryDao.get(t1.getUuid());
		inventory.setChecker(uuid);
		inventory.setChecktime(new Date());
		inventory.setRemark(t1.getRemark());
		inventory.setState(Inventory.STATE_CHECK);

		// 更新库存表
		Storedetail storedetail = new Storedetail();
		storedetail.setGoodsuuid(inventory.getGoodsuuid());
		storedetail.setStoreuuid(inventory.getStoreuuid());
		List<Storedetail> list = storedetailDao.findList(storedetail, null, null);

		if (list.size() < 1) { // 没有匹配数据 抛异常
			throw new ErpException("出现错误");
		} else if (inventory.getNum() > 0) {
			Storedetail getStoredetail = list.get(0);
			if (inventory.getType().equals("1")) {
				getStoredetail.setNum(getStoredetail.getNum() + inventory.getNum());
			}
			if (inventory.getType().equals("2")) {
				getStoredetail.setNum(getStoredetail.getNum() - inventory.getNum());
			}

			// 库存变更记录
			Storeoper storeoper = new Storeoper();
			storeoper.setEmpuuid(uuid);
			storeoper.setStoreuuid(inventory.getStoreuuid());
			storeoper.setGoodsuuid(inventory.getGoodsuuid());
			storeoper.setNum(inventory.getNum());
			storeoper.setOpertime(inventory.getChecktime());
			if (inventory.getType().equals("1")) {
				storeoper.setType(Storeoper.INVENTORY_TYPE_IN);
			}
			if (inventory.getType().equals("2")) {
				storeoper.setType(Storeoper.INVENTORY_TYPE_OUT);
			}
			storeoperDao.save(storeoper);
		}
	}

	public void setInventoryDao(IInventoryDao inventoryDao) {
		this.inventoryDao = inventoryDao;
		super.setBaseDAO(this.inventoryDao);
	}

	public void setEmpDao(IEmpDao empDao) {
		this.empDao = empDao;
	}

	public void setStoreDao(IStoreDao storeDao) {
		this.storeDao = storeDao;
	}

	public void setGoodsDao(IGoodsDao goodsDao) {
		this.goodsDao = goodsDao;
	}

	public void setStoredetailDao(IStoredetailDao storedetailDao) {
		this.storedetailDao = storedetailDao;
	}

	public void setStoreoperDao(IStoreoperDao storeoperDao) {
		this.storeoperDao = storeoperDao;
	}

}
