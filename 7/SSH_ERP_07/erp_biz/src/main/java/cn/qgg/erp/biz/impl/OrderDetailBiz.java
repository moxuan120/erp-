package cn.qgg.erp.biz.impl;

import cn.qgg.erp.biz.IOrderDetailBiz;
import cn.qgg.erp.dao.IOrderDetailDao;
import cn.qgg.erp.dao.IStoredetailDao;
import cn.qgg.erp.dao.IStoreoperDao;
import cn.qgg.erp.dao.ISupplierDao;
import cn.qgg.erp.entity.Orderdetail;
import cn.qgg.erp.entity.Orders;
import cn.qgg.erp.entity.Storedetail;
import cn.qgg.erp.entity.Storeoper;
import cn.qgg.erp.entity.Supplier;
import cn.qgg.erp.exception.ErpException;
import cn.qgg.redsun.bos.ws.impl.IWaybillWs;

import java.util.Date;
import java.util.List;

public class OrderDetailBiz extends BaseBiz<Orderdetail> implements IOrderDetailBiz {
    private IOrderDetailDao orderDetailDao;
    private IStoredetailDao storedetailDao;
    private IStoreoperDao storeoperDao;
    private IWaybillWs waybillWs;
    private ISupplierDao supplierDao;


    /**
     * 入库
     *
     * @param uuid      订单详情id
     * @param empUuid   操作员id
     * @param storeUuid 仓库id
     */
    @Override
    public void doInStore(Long uuid, Long empUuid, Long storeUuid) {
        Orderdetail orderdetail = orderDetailDao.get(uuid);
        Orders orders = orderdetail.getOrders();
        //状态判断
        if (!Orderdetail.STATE_NOT_IN.equals(orderdetail.getState())) {
            throw new ErpException("该明细已经入库了!");
        }
        if (!Orders.STATE_START.equals(orders.getState())) {
            throw new ErpException("该订单已经入库了!");
        }

        //1：更新订单详情数据
        orderdetail.setEnder(empUuid);
        orderdetail.setEndtime(new Date());
        orderdetail.setStoreuuid(storeUuid);
        orderdetail.setState(Orderdetail.STATE_IN);

        //2：更新库存表
        Storedetail storedetail = new Storedetail();
        storedetail.setGoodsuuid(orderdetail.getGoodsuuid());
        storedetail.setStoreuuid(storeUuid);
        List<Storedetail> list = storedetailDao.findList(storedetail, null, null);
        //没有匹配数据增加
        if (list.size() == 0) {
            storedetail.setNum(orderdetail.getNum());
            storedetailDao.save(storedetail);
        } else {//存在匹配数据累加数量
            Storedetail getStoredetail = list.get(0);
            getStoredetail.setNum(getStoredetail.getNum() + orderdetail.getNum());
        }

        //3：仓库操作记录
        Storeoper storeoper = new Storeoper();
        storeoper.setEmpuuid(empUuid);
        storeoper.setStoreuuid(storeUuid);
        storeoper.setGoodsuuid(orderdetail.getGoodsuuid());
        storeoper.setNum(orderdetail.getNum());
        storeoper.setOpertime(orderdetail.getEndtime());
        storeoper.setType(Storeoper.TYPE_IN);
        storeoperDao.save(storeoper);

        //4：更新订单的状态
        Orderdetail od = new Orderdetail();
        od.setState(Orderdetail.STATE_NOT_IN);
        od.setOrders(orders);
        //待入库子项为零设置
        Long count = orderDetailDao.findCount(od, null, null);
        if (count < 1) {
            orders.setState(Orders.STATE_END);
            orders.setEndtime(orderdetail.getEndtime());
            orders.setEnder(empUuid);
            //自动提交物流订单
            Supplier supplier = supplierDao.get(orders.getSupplieruuid());
            Long waybillSn = waybillWs.addWwaybill(1L, supplier.getAddress(), supplier.getName(), supplier.getTele(), "---");
            orders.setWaybillsn(waybillSn);
        }
    }


    /**
     * 出库
     *
     * @param uuid      订单详情id
     * @param empUuid   操作员id
     * @param storeUuid 仓库id
     */
    @Override
    public void doOutStore(Long uuid, Long empUuid, Long storeUuid) {
        Orderdetail orderdetail = orderDetailDao.get(uuid);
        Orders orders = orderdetail.getOrders();
        //状态判断
        if (!Orderdetail.STATE_NOT_OUT.equals(orderdetail.getState())) {
            throw new ErpException("该明细已经出库了!");
        }
        if (!Orders.STATE_NOT_OUT.equals(orders.getState())) {
            throw new ErpException("该订单已经出库了!");
        }

        //1：更新订单详情数据
        orderdetail.setEnder(empUuid);
        orderdetail.setEndtime(new Date());
        orderdetail.setStoreuuid(storeUuid);
        orderdetail.setState(Orderdetail.STATE_OUT);

        //2：更新库存表
        Storedetail storedetail = new Storedetail();
        storedetail.setGoodsuuid(orderdetail.getGoodsuuid());
        storedetail.setStoreuuid(storeUuid);
        List<Storedetail> list = storedetailDao.findList(storedetail, null, null);
        //减少数量
        long num = -1L;
        if (list.size() > 0) {
            Storedetail getStoredetail = list.get(0);
            num = getStoredetail.getNum() - orderdetail.getNum();
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
        storeoper.setGoodsuuid(orderdetail.getGoodsuuid());
        storeoper.setNum(orderdetail.getNum());
        storeoper.setOpertime(orderdetail.getEndtime());
        storeoper.setType(Storeoper.TYPE_OUT);
        storeoperDao.save(storeoper);

        //4：更新订单的状态
        Orderdetail od = new Orderdetail();
        od.setState(Orderdetail.STATE_NOT_OUT);
        od.setOrders(orders);
        //待入库子项为零设置
        Long count = orderDetailDao.findCount(od, null, null);
        if (count < 1) {
            orders.setState(Orders.STATE_OUT);
            orders.setEnder(empUuid);
            orders.setEndtime(orderdetail.getEndtime());
            //自动提交物流订单
            Supplier supplier = supplierDao.get(orders.getSupplieruuid());
            Long waybillSn = waybillWs.addWwaybill(supplier.getUuid(), supplier.getAddress(), supplier.getName(), supplier.getTele(), "---");
            orders.setWaybillsn(waybillSn);
        }
    }

    public void setOrderDetailDao(IOrderDetailDao orderDetailDao) {
        this.orderDetailDao = orderDetailDao;
        super.setBaseDAO(orderDetailDao);
    }

    public void setStoredetailDao(IStoredetailDao storedetailDao) {
        this.storedetailDao = storedetailDao;
    }

    public void setStoreoperDao(IStoreoperDao storeoperDao) {
        this.storeoperDao = storeoperDao;
    }

    public void setWaybillWs(IWaybillWs waybillWs) {
        this.waybillWs = waybillWs;
    }

    public void setSupplierDao(ISupplierDao supplierDao) {
        this.supplierDao = supplierDao;
    }
}
