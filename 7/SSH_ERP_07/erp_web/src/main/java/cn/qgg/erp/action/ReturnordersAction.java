package cn.qgg.erp.action;

import cn.qgg.erp.biz.IOrderDetailBiz;
import cn.qgg.erp.biz.IOrdersBiz;
import cn.qgg.erp.biz.IReturnordersBiz;
import cn.qgg.erp.entity.Emp;
import cn.qgg.erp.entity.Orderdetail;
import cn.qgg.erp.entity.Orders;
import cn.qgg.erp.entity.Returnorderdetail;
import cn.qgg.erp.entity.Returnorders;
import cn.qgg.erp.exception.ErpException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 退货订单Action
 * 
 * @author Administrator
 *
 */
public class ReturnordersAction extends BaseAction<Returnorders> {

	private IReturnordersBiz returnordersBiz;
	private IOrdersBiz ordersBiz;
	private IOrderDetailBiz orderDetailBiz;
	private Long num;
	private String type;
	
	
	/**
	 * 整个订单退货
	 *
	 * @return
	 */
	@Override
	public String add() {
		Emp user = super.getUser();
		if (user == null) {
			ajaxReturn(false, "请登陆后再操作");
			return SUCCESS;
		}
		try {
			// 获取要退货的订单，设置订单状态
			Orders orders = ordersBiz.get(getId());
			if (Integer.parseInt(orders.getState()) > 3){
				throw new ErpException("此订单已经申请退货，不能重复申请");
			}
			orders.setState(Orders.STATE_RETURN_CREATE);
			// 生成一个退货订单
			Returnorders returnorders = new Returnorders();
			returnorders.setCreater(user.getUuid());
			returnorders.setSupplieruuid(orders.getSupplieruuid());
			returnorders.setOrders(orders);
			returnorders.setType(type);
			// 把所有子订单退货
			List<Orderdetail> orderdetails = orders.getOrderdetails();
			List<Returnorderdetail> returnorderdetails = returnorders.getReturnorderdetails();
			Returnorderdetail returnorderdetail = null;
			for (Orderdetail orderdetail : orderdetails) {
				orderdetail.setState(Orderdetail.STATE_RETURN_CREATE);// 订单详情状态
				// 复制订单信息
				returnorderdetail = new Returnorderdetail();
				returnorderdetail.setGoodsuuid(orderdetail.getGoodsuuid());
				returnorderdetail.setGoodsname(orderdetail.getGoodsname());
				returnorderdetail.setPrice(orderdetail.getPrice());
				returnorderdetail.setNum(orderdetail.getNum());
				returnorderdetail.setMoney(orderdetail.getMoney());
				returnorderdetail.setState(Returnorderdetail.STATE_START);
				// 关联
				returnorderdetail.setReturnorders(returnorders);
				returnorderdetails.add(returnorderdetail);
			}
			// 添加详情
			returnorders.setReturnorderdetails(returnorderdetails);
			super.setT1(returnorders);
			super.add();
			ajaxReturn(true, "操作成功");
		} catch (ErpException e) {
			e.printStackTrace();
			ajaxReturn(false, "操作失败 ："+e.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
			ajaxReturn(false, "操作失败");
		}
		return SUCCESS;
	}
	
	
	/**
	 * 退货一个子订单
	 *
	 * @return
	 */
	public String addDetail() {
		Emp user = super.getUser();
		if (user == null) {
			ajaxReturn(false, "请登陆后再操作");
			return SUCCESS;
		}

		try {
			// 获取要退货的订单，设置订单状态
			Orderdetail orderdetail = orderDetailBiz.get(getId());
			if (Integer.parseInt(orderdetail.getState()) > 1){
				throw new ErpException("此订单已经申请退货，不能重复退货");
			}
			orderdetail.setState(Orderdetail.STATE_RETURN_CREATE);
			Orders orders = orderdetail.getOrders();
			orders.setState(Orders.STATE_RETURNDETAIL_CREATE);
			
			// 生成一个退货订单
			Returnorders returnorders = new Returnorders();
			returnorders.setCreater(user.getUuid());
			returnorders.setSupplieruuid(orders.getSupplieruuid());
			returnorders.setOrders(orders);
			returnorders.setType(type);
			
			// 复制订单信息
			Returnorderdetail returnorderdetail = new Returnorderdetail();
			returnorderdetail.setGoodsuuid(orderdetail.getGoodsuuid());
			returnorderdetail.setPrice(orderdetail.getPrice());
			returnorderdetail.setNum(num);
			returnorderdetail.setMoney(orderdetail.getPrice() * num);
			returnorderdetail.setState(Returnorderdetail.STATE_START);
			// 关联
			returnorderdetail.setReturnorders(returnorders);
			List<Returnorderdetail> returnorderdetails = new ArrayList<Returnorderdetail>();
			returnorderdetails.add(returnorderdetail);
			returnorders.setReturnorderdetails(returnorderdetails);
			super.setT1(returnorders);
			super.add();
			ajaxReturn(true, "操作成功");
		} catch (ErpException e) {
			e.printStackTrace();
			ajaxReturn(false, "操作失败 ："+e.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
			ajaxReturn(false, "操作失败");
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
			returnordersBiz.doCheck(super.getId(), user.getUuid());
			ajaxReturn(true, "审核成功");
		} catch (Exception e) {
			e.printStackTrace();
			ajaxReturn(false, "审核失败");
		}

		return SUCCESS;
	}

	/**
	 * 登陆账户的list
	 *
	 * @return
	 * @throws IOException
	 */
	public String myList() throws IOException {
		Returnorders returnorders = getT1();
		returnorders.setCreater(super.getUser().getUuid());
		return super.list();
	}

	public void setReturnordersBiz(IReturnordersBiz returnordersBiz) {
		this.returnordersBiz = returnordersBiz;
		super.setBaseBiz(this.returnordersBiz);
	}

	public void setOrdersBiz(IOrdersBiz ordersBiz) {
		this.ordersBiz = ordersBiz;
	}


	public void setNum(Long num) {
		this.num = num;
	}


	public void setType(String type) {
		this.type = type;
	}


	public void setOrderDetailBiz(IOrderDetailBiz orderDetailBiz) {
		this.orderDetailBiz = orderDetailBiz;
	}
}
