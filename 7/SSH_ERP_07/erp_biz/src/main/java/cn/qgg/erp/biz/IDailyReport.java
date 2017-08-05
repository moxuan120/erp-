package cn.qgg.erp.biz;

import cn.qgg.erp.entity.Storeoper;

import java.util.List;
import java.util.Map;

public interface IDailyReport {

	/**
	 * 根据时间段找到每日报表详情
	 * 
	 */
	List<Map<String, Object>> findList(Storeoper t1, Storeoper t2, Object param);
	
	/**
	 * 每日报表的进出库类型总金额
	 * @return
	 */
	List<Map<String, Object>>  storeoperReport(Storeoper t1, Storeoper t2, Object param);
}
