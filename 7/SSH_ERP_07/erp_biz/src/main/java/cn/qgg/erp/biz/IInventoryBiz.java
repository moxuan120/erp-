package cn.qgg.erp.biz;
import cn.qgg.erp.entity.Inventory;
/**
 * 盘盈盘亏业务逻辑层接口
 * @author Administrator
 *
 */
public interface IInventoryBiz extends IbaseBiz<Inventory>{

	void doCheck(Inventory t1, Long uuid);

}

