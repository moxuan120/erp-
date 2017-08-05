package cn.qgg.erp.action;

import cn.qgg.erp.biz.IReportBiz;

import java.io.IOException;
import java.io.Writer;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSON;
public class ReportAction {
    private IReportBiz reportBiz;
    private List report;//json返回
    private Date date1;//开始时间
    private Date date2;//结束时间
    private int year;
    private String type;

    public String orderReport() throws IOException {
        report = reportBiz.orderReport(date1, date2,type);
        return "report";
    }
    
    //销售退货
    public String returnOrders(){
    	report = reportBiz.returnOrders(date1,date2,type);
    	return "report";
    }
    

    public String trendReport() {
        if(year == 0) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            year = calendar.get(Calendar.YEAR);
        }
        report = reportBiz.trendReport(year,type);
        return "report";
    }
    
    /**
     *销售退货趋势分析
     * @return
     */
    public String sellTrendReport() {
        if(year == 0) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            year = calendar.get(Calendar.YEAR);
        }
        System.out.println("type         --------------------------:"+ type);
        report = reportBiz.sellTrendReport(year,type);
        return "report";
    }
    
    /**
     * 仓库进出货记录
	 * @param year ： 年份
	 * @param type ：进/出
	 * @return 
     * @return
     */
    public String getInOutStore(){
    	if(year == 0) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            year = calendar.get(Calendar.YEAR);
        }
    	 report = reportBiz.getInOutStore(year,type);
         return "report";
    }

    public void setReportBiz(IReportBiz reportBiz) {
        this.reportBiz = reportBiz;
    }

    public List getReport() {
        return report;
    }

    public void setDate1(Date date1) {
        this.date1 = date1;
    }

    public void setDate2(Date date2) {
        this.date2 = date2;
    }

    public void setYear(int year) {
        this.year = year;
    }
    
    public void setType(String type) {
		this.type = type;
	}
}
