package cn.qgg.erp.action;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils.Null;

import cn.qgg.erp.biz.IInventoryBiz;
import cn.qgg.erp.biz.IStoredetailBiz;
import cn.qgg.erp.entity.Emp;
import cn.qgg.erp.entity.Inventory;
import cn.qgg.erp.entity.Storedetail;
import cn.qgg.erp.exception.ErpException;

/**
 * 盘盈盘亏Action
 * 
 * @author Administrator
 *
 */
public class InventoryAction extends BaseAction<Inventory> {

	private IInventoryBiz inventoryBiz;
	private IStoredetailBiz storedetailBiz;

	/**
	 * @return
	 * @throws Exception
	 *             增加一个数据
	 */
	public String add() throws Exception {
		Emp user = super.getUser();
		if (user == null) {
			ajaxReturn(false, "请登陆后再操作");
			return SUCCESS;
		}
		try {
			Inventory inventory = getT1();
			inventory.setCreater(getUser().getUuid());
			inventory.setCreatetime(new Date());
			inventory.setState(Inventory.STATE_CREATE);

			// 查询数据库库存
			Long dbNum = 0L;
			Storedetail storedetail1 = new Storedetail();
			storedetail1.setStoreuuid(inventory.getStoreuuid());
			storedetail1.setGoodsuuid(inventory.getGoodsuuid());
			List<Storedetail> storedetail = storedetailBiz.findList(storedetail1, null, null);
			if (storedetail.size() > 0)
				dbNum = storedetail.get(0).getNum();
			if (dbNum == 0L)
				throw new ErpException("数据库没有此库存信息，如为遗漏请补入库");
			if (inventory.getNum() < dbNum) {
				inventory.setType(Inventory.TYPE_LOSS);
				inventory.setNum(dbNum - inventory.getNum());
			} else if (inventory.getNum() > dbNum) {
				inventory.setType(Inventory.TYPE_PROFIT);
				inventory.setNum(inventory.getNum() - dbNum);
			} else {
				inventory.setType(Inventory.TYPE_GOOD);
				inventory.setNum(0L);
			}

			super.add();

			ajaxReturn(true, "保存成功");
		} catch (ErpException e) {
			e.printStackTrace();
			ajaxReturn(false, "保存失败：" + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			ajaxReturn(false, "保存失败");
		}
		return SUCCESS;
	}

	/**
	 * 审核订单
	 *
	 * @return
	 */
	public String doCheck() {
		Emp user = super.getUser();
		if (user == null) {
			ajaxReturn(false, "您没有登陆");
			return SUCCESS;
		}
		try {
			inventoryBiz.doCheck(super.getT1(), user.getUuid());
			ajaxReturn(true, "审核成功");
		} catch (Exception e) {
			e.printStackTrace();
			ajaxReturn(false, "审核失败");
		}

		return SUCCESS;
	}

	public void setInventoryBiz(IInventoryBiz inventoryBiz) {
		this.inventoryBiz = inventoryBiz;
		super.setBaseBiz(this.inventoryBiz);
	}

	public void setStoredetailBiz(IStoredetailBiz storedetailBiz) {
		this.storedetailBiz = storedetailBiz;
	}

}
