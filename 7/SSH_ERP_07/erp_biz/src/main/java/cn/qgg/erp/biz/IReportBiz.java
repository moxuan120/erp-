package cn.qgg.erp.biz;

import java.util.Date;
import java.util.List;

/**
 * 报表
 */
public interface IReportBiz {

    /**
     * 销售报表
     *
     * @return
     */
    List orderReport(Date date1, Date date2,String type);

    List trendReport(int year, String type);
    //销售退货
    public List returnOrders(Date date1,Date date2,String type);
    //退货趋势
	List sellTrendReport(int year, String type);
	/**
	 * 仓库进出货记录
	 * @param year ： 年份
	 * @param type ：进/出
	 * @return
	 */
	List getInOutStore(int year,String type);
}
