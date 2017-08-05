package cn.qgg.erp.biz;
import cn.qgg.erp.entity.Returnorders;
/**
 * 退货订单业务逻辑层接口
 * @author Administrator
 *
 */
public interface IReturnordersBiz extends IbaseBiz<Returnorders>{
	/**
     * 审核退货订单
     *
     * @param uuid    订单编号
     * @param empuuid 员工编号
     */
	void doCheck(Long uuid, Long empuuid);

}

