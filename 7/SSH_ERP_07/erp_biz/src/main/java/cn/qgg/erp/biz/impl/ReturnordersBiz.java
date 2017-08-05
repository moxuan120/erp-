package cn.qgg.erp.biz.impl;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.Order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.qgg.erp.biz.IReturnordersBiz;
import cn.qgg.erp.dao.IEmpDao;
import cn.qgg.erp.dao.IReturnordersDao;
import cn.qgg.erp.dao.ISupplierDao;
import cn.qgg.erp.entity.Orderdetail;
import cn.qgg.erp.entity.Orders;
import cn.qgg.erp.entity.Returnorderdetail;
import cn.qgg.erp.entity.Returnorders;
import cn.qgg.erp.exception.ErpException;
/**
 * 退货订单业务逻辑类
 * @author Administrator
 *
 */
public class ReturnordersBiz extends BaseBiz<Returnorders> implements IReturnordersBiz {

	private static final Logger log = LoggerFactory.getLogger(OrdersBiz.class);
	private IReturnordersDao returnordersDao;
	private IEmpDao empDao;
	private ISupplierDao supplierDao;
	
	
	public void setSupplierDao(ISupplierDao supplierDao) {
		this.supplierDao = supplierDao;
	}

	public void setEmpDao(IEmpDao empDao) {
		this.empDao = empDao;
	}

	public void setReturnordersDao(IReturnordersDao returnordersDao) {
		this.returnordersDao = returnordersDao;
		super.setBaseDAO(this.returnordersDao);
	}

	/**
     * 审核退货订单
     *
     * @param uuid    订单编号
     * @param empuuid 员工编号
     */
    @Override
    public void doCheck(Long uuid, Long empuuid) {
        Returnorders returnOrders = returnordersDao.get(uuid);
        if (!Returnorders.STATE_CREATE.equals(returnOrders.getState())) {
            throw new ErpException("该订单已经审核，不能重复审核");
        }
        //设置审核信息
        returnOrders.setChecker(empuuid);
        returnOrders.setChecktime(new Date());
        returnOrders.setState(Returnorders.STATE_CHECK);
        //改变原订单状态
        Orders orders = returnOrders.getOrders();
        if (orders.getState().equals(Orders.STATE_RETURN_CREATE)) {
			orders.setState(Orders.STATE_RETURN_CHECK);
		}
        if (orders.getState().equals(Orders.STATE_RETURNDETAIL_CREATE)) {
        	orders.setState(Orders.STATE_RETURNDETAIL_CHECK);
        }
        //改变原订单子项
        List<Orderdetail> orderdetails = orders.getOrderdetails();
        for (Orderdetail orderdetail : orderdetails) {
			if (orderdetail.getState().equals(Orderdetail.STATE_RETURN_CREATE)) {
				orderdetail.setState(Orderdetail.STATE_RETURN_CHECK);
			}
		}
    }
    
	
	/**
	 * 添加
	 */
	@Override
    public void add(Returnorders returnorders) {
		//补全信息
		returnorders.setCreatetime(new Date());//生成时间
		returnorders.setState(Returnorders.STATE_CREATE);//未审核
		//计算总价
        double total = 0.0;
        for (Returnorderdetail returnorderdetail : returnorders.getReturnorderdetails()) {
            total += returnorderdetail.getMoney();
        }
        returnorders.setTotalmoney(total);
        log.info("添加一笔订单，订单类型为：{}", (returnorders.getType().equals("1") ? "采购退货" : "销售退货"));
        
        super.add(returnorders);
    }
	
	/**
	 * 
	 */
	 @Override
    public List<Returnorders> findPageList(Returnorders t1, Returnorders t2, Object param, int firstResult, int maxResult) {

        List<Returnorders> returnorders = super.findPageList(t1, t2, param, firstResult, maxResult);
        log.info("查询{}条订单信息",returnorders.size());
        //补上这些操作员的名称和供应商的名称
        for (Returnorders returnorder : returnorders) {
        	returnorder.setCreaterName(getName(returnorder.getCreater(), empDao));
        	returnorder.setCheckerName(getName(returnorder.getChecker(), empDao));
        	returnorder.setEnderName(getName(returnorder.getEnder(), empDao));
        	returnorder.setSupplierName(getName(returnorder.getSupplieruuid(), supplierDao));
        }
        log.info("补全Returnorders的操作员名称和供应商名称");
        return returnorders;
    }

	
	
	
}
