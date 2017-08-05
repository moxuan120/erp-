package cn.qgg.erp.biz;
import cn.qgg.erp.entity.Returnorderdetail;
/**
 * 退货订单明细业务逻辑层接口
 * @author Administrator
 *
 */
public interface IReturnorderdetailBiz extends IbaseBiz<Returnorderdetail>{
	/**
	 * 销售退货
	 * @param uuid ：退货明细ID
	 * @param empuuid ：用户ID
	 * @param storeuuid ：仓库ID
	 */
	void doReturnInStore(Long uuid, Long empuuid, Long storeuuid);

	void doOutStore(Long uuid, Long empUuid, Long storeUuid);

}

