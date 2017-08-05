package cn.qgg.erp.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface IReportDao {
    /**
     * 销售报表
     *
     * @return
     */
    List orderReport();
    List orderReport(Date date1, Date date2,String type);

    //销售退货
    List returnOrders(Date date1,Date date2,String type);

    List<Map<String, Object>> getSumMoney(int year, String type);
	List<Map<String, Object>> getSellMoney(int year, String type);
	/**
	 * 仓库进出货记录
	 * @param year ： 年份
	 * @param type ：进/出
	 * @return
	 */
	List<Map<String, Object>> getInOutStore(int year,String type);
}
