package cn.qgg.erp.biz;

import cn.qgg.erp.entity.Orders;

import java.io.OutputStream;

public interface IOrdersBiz extends IbaseBiz<Orders> {

    /**
     * 审核订单
     *
     * @param uuid    订单编号
     * @param empuuid 员工编号
     */
    void doCheck(Long uuid, Long empuuid);

    /**
     * 确认订单
     *
     * @param uuid    订单编号
     * @param empuuid 员工编号
     */
    void doStart(long uuid, Long empuuid);

    void exportExcel(OutputStream ops, Long uuid);
}
