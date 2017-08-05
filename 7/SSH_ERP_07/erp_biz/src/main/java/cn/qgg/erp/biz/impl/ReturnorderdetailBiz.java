package cn.qgg.erp.biz.impl;
import java.util.Date;
import java.util.List;

import cn.qgg.erp.biz.IReturnorderdetailBiz;
import cn.qgg.erp.dao.IReturnorderdetailDao;
import cn.qgg.erp.dao.IStoredetailDao;
import cn.qgg.erp.dao.IStoreoperDao;
import cn.qgg.erp.dao.ISupplierDao;
import cn.qgg.erp.entity.Orderdetail;
import cn.qgg.erp.entity.Orders;
import cn.qgg.erp.entity.Returnorderdetail;
import cn.qgg.erp.entity.Returnorders;
import cn.qgg.erp.entity.Storedetail;
import cn.qgg.erp.entity.Storeoper;
import cn.qgg.erp.entity.Supplier;
import cn.qgg.erp.exception.ErpException;
import cn.qgg.redsun.bos.ws.impl.IWaybillWs;

/**
 * 退货订单明细业务逻辑类
 * @author Administrator
 *
 */
public class ReturnorderdetailBiz extends BaseBiz<Returnorderdetail> implements IReturnorderdetailBiz {

	private IReturnorderdetailDao returnorderdetailDao;
	//仓库明细
	private IStoredetailDao storedetailDao;
	//仓库操作记录
	private IStoreoperDao storeoperDao;
	//发快递
	private ISupplierDao supplierDao;
	private IWaybillWs waybillWs;
	
	@Override
	/**
	 * 销售退货入库
	 * @param uuid ：退货明细ID
	 * @param empuuid ：用户ID
	 * @param storeuuid ：仓库ID
	 */
	public void doReturnInStore(Long uuid, Long empuuid, Long storeuuid) {
		//获取明细详情
		Returnorderdetail returnorderdetail = returnorderdetailDao.get(uuid);
		//获取退货单
		Returnorders returnorders = returnorderdetail.getReturnorders();
		//判断状态
		if (!Returnorderdetail.STATE_START.equals(returnorderdetail.getState())) {
			throw new ErpException("该明细已入库了");
		}
		if (!Returnorders.STATE_CHECK.equals(returnorders.getState())) {
			throw new ErpException("该订单已入库了");
		}
		
		//1：更新明细数据
		returnorderdetail.setEnder(empuuid);
		returnorderdetail.setEndtime(new Date());
		returnorderdetail.setStoreuuid(storeuuid);
		returnorderdetail.setState(Returnorderdetail.STATE_END);
		
		//2：更新库存表
		Storedetail storedetail = new Storedetail();
		storedetail.setGoodsuuid(returnorderdetail.getGoodsuuid());
		storedetail.setStoreuuid(storeuuid);
		List<Storedetail> list = storedetailDao.findList(storedetail, null, null);
		if (list.size() == 0) {
			storedetail.setNum(returnorderdetail.getNum());
			storedetailDao.save(storedetail);
		}else {
			Storedetail storedetail2 = list.get(0);
			storedetail2.setNum(storedetail2.getNum() + returnorderdetail.getNum());
		}
		
		//3：仓库操作记录
		Storeoper storeoper = new Storeoper();
		storeoper.setEmpuuid(empuuid);
		storeoper.setStoreuuid(storeuuid);
		storeoper.setGoodsuuid(returnorderdetail.getGoodsuuid());
		storeoper.setNum(returnorderdetail.getNum());
		storeoper.setOpertime(returnorderdetail.getEndtime());
		storeoper.setType(Storeoper.RETURN_TYPE_IN);
		storeoperDao.save(storeoper);
		
		//4:更新订单状态
		Returnorderdetail rtnOd = new Returnorderdetail();
		rtnOd.setState(Returnorderdetail.STATE_START);
		rtnOd.setReturnorders(returnorders);
		Long count = returnorderdetailDao.findCount(rtnOd, null, null);
		if (count < 1) {
			returnorders.setState(Returnorders.STATE_END);
			returnorders.setEndtime(returnorderdetail.getEndtime());
			returnorders.setEnder(empuuid);
		}
		
		//更新原订单状态
		Orders orders = returnorders.getOrders();
		if (orders.getState().equals(Orders.STATE_RETURN_CHECK)) {
			orders.setState(Orders.STATE_RETURN_END);
		}
        if (orders.getState().equals(Orders.STATE_RETURNDETAIL_CHECK)) {
        	orders.setState(Orders.STATE_RETURNDETAIL_END);
        }
        ////更新原orderDetail状态
		List<Orderdetail> orderdetails = orders.getOrderdetails();
		for (Orderdetail orderdetail : orderdetails) {
			if (orderdetail.getState().equals(Orderdetail.STATE_RETURN_CHECK)) {
				orderdetail.setState(Orderdetail.STATE_RETURN_END);
			}
		}
	}


	/**
	 * 退货出库
	 */
	@Override
	public void doOutStore(Long uuid, Long empUuid, Long storeUuid) {
		 Returnorderdetail returnorderdetail = returnorderdetailDao.get(uuid);
	        Returnorders returnorders = returnorderdetail.getReturnorders();
	        Orders orders = returnorders.getOrders();
	        //状态判断
	        if (!Returnorderdetail.STATE_START.equals(returnorderdetail.getState())) {
	            throw new ErpException("该明细已经出库了!");
	        }
	        if (!Returnorders.STATE_CHECK.equals(returnorders.getState())) {
	            throw new ErpException("该订单已经出库了!");
	        }

	        //1：更新订单详情数据
	        returnorderdetail.setEnder(empUuid);
	        returnorderdetail.setEndtime(new Date());
	        returnorderdetail.setStoreuuid(storeUuid);
	        returnorderdetail.setState(Orderdetail.STATE_RETURN_END);

	        //2：更新库存表
	        Storedetail storedetail = new Storedetail();
	        storedetail.setGoodsuuid(returnorderdetail.getGoodsuuid());
	        storedetail.setStoreuuid(storeUuid);
	        List<Storedetail> list = storedetailDao.findList(storedetail, null, null);
	        //减少数量
	        long num = -1L;
	        if (list.size() > 0) {
	            Storedetail gotStoredetail = list.get(0);
	            num = gotStoredetail.getNum() - returnorderdetail.getNum();
	        }
	        if (num > 0) {//库存充足
	            storedetail.setNum(num);
	        } else {//库存不足
	            throw new ErpException("库存不足!");
	        }

	        //3：仓库操作记录
	        Storeoper storeoper = new Storeoper();
	        storeoper.setEmpuuid(empUuid);
	        storeoper.setStoreuuid(storeUuid);
	        storeoper.setGoodsuuid(returnorderdetail.getGoodsuuid());
	        storeoper.setNum(returnorderdetail.getNum());
	        storeoper.setOpertime(returnorderdetail.getEndtime());
	        storeoper.setType(Storeoper.RETURN_TYPE_OUT);
	        storeoperDao.save(storeoper);

	        //4：更新订单的状态
	        Returnorderdetail rod = new Returnorderdetail();
	        rod.setState(Returnorderdetail.STATE_START);
	        rod.setReturnorders(returnorders);
	        //待入库子项为零设置
	        Long count = returnorderdetailDao.findCount(rod, null, null);
	        if (count < 1) {
	        	returnorders.setState(Returnorders.STATE_END);
	        	returnorders.setEnder(empUuid);
	        	returnorders.setEndtime(returnorderdetail.getEndtime());
	            //自动提交物流订单
	            Supplier supplier = supplierDao.get(orders.getSupplieruuid());
	            Long waybillSn = waybillWs.addWwaybill(supplier.getUuid(), supplier.getAddress(), supplier.getName(), supplier.getTele(), "---");
	            orders.setWaybillsn(waybillSn);
	        }
	        
	        //更新原订单状态
			if (orders.getState().equals(Orders.STATE_RETURN_CHECK)) {
				orders.setState(Orders.STATE_RETURN_END);
			}
	        if (orders.getState().equals(Orders.STATE_RETURNDETAIL_CHECK)) {
	        	orders.setState(Orders.STATE_RETURNDETAIL_END);
	        }
	        ////更新原orderDetail状态
			List<Orderdetail> orderdetails = orders.getOrderdetails();
			for (Orderdetail orderdetail : orderdetails) {
				if (orderdetail.getState().equals(Orderdetail.STATE_RETURN_CHECK)) {
					orderdetail.setState(Orderdetail.STATE_RETURN_END);
				}
			}
		
	}

	public void setReturnorderdetailDao(IReturnorderdetailDao returnorderdetailDao) {
		this.returnorderdetailDao = returnorderdetailDao;
		super.setBaseDAO(this.returnorderdetailDao);
	}
	
	public void setStoredetailDao(IStoredetailDao storedetailDao) {
		this.storedetailDao = storedetailDao;
	}

	public void setStoreoperDao(IStoreoperDao storeoperDao) {
		this.storeoperDao = storeoperDao;
	}

	public void setSupplierDao(ISupplierDao supplierDao) {
		this.supplierDao = supplierDao;
	}


	public void setWaybillWs(IWaybillWs waybillWs) {
		this.waybillWs = waybillWs;
	}
	
}
