package cn.qgg.erp.biz;

import cn.qgg.erp.entity.Orderdetail;

public interface IOrderDetailBiz extends IbaseBiz<Orderdetail> {

    /**
     * 入库
     *
     * @param uuid      订单详情id
     * @param empUuid   操作员id
     * @param storeUuid 仓库id
     */
    void doInStore(Long uuid, Long empUuid, Long storeUuid);

    /**
     * 出库
     *
     * @param uuid      订单详情id
     * @param empUuid   操作员id
     * @param storeUuid 仓库id
     */
    void doOutStore(Long uuid, Long empUuid, Long storeUuid);
}
