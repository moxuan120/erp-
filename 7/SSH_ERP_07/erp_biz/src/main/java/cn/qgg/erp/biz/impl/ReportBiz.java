package cn.qgg.erp.biz.impl;

import cn.qgg.erp.biz.IReportBiz;
import cn.qgg.erp.dao.IReportDao;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ReportBiz implements IReportBiz {
    private IReportDao reportDao;

    @Override
    public List orderReport(Date date1, Date date2,String type) {
        return reportDao.orderReport(date1, date2,type);
    }

    @Override
    public List trendReport(int year, String type) {
        List<Map<String, Object>> sumMoney = reportDao.getSumMoney(year,type);

        //转换成Map<月份，数据>格式
        Map<Integer, Object> existMap = new HashMap<>();
        for (Map<String, Object> map : sumMoney) {
            existMap.put((Integer) map.get("month"), map.get("y"));
        }

        //月份列表
        String[] months = new String[]{"", "一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"};

        //按1-12月封装数据，没有的月份置零
        List<Map<String, Object>> list = new ArrayList<>(12);
        for (int i = 1; i < 13; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("name", months[i]);
            map.put("y", existMap.get(i) == null ? 0 : existMap.get(i));
            list.add(map);
        }
        return list;
    }
    
    @Override
	public List sellTrendReport(int year, String type) {
    	 List<Map<String, Object>> sumMoney = reportDao.getSellMoney(year,type);

         //转换成Map<月份，数据>格式
         Map<Integer, Object> existMap = new HashMap<>();
         for (Map<String, Object> map : sumMoney) {
             existMap.put((Integer) map.get("month"), map.get("y"));
         }

         //月份列表
         String[] months = new String[]{"", "一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"};

         //按1-12月封装数据，没有的月份置零
         List<Map<String, Object>> list = new ArrayList<>(12);
         for (int i = 1; i < 13; i++) {
             Map<String, Object> map = new HashMap<>();
             map.put("name", months[i]);
             map.put("y", existMap.get(i) == null ? 0 : existMap.get(i));
             list.add(map);
         }
         
         return list;
	}
    
    /**
	 * 仓库进出货记录
	 * @param year ： 年份
	 * @param type ：进/出
	 * @return
	 */
	public List getInOutStore(int year,String type){
		 // string ： 月  || object ：数量
		 List<Map<String, Object>> sum = reportDao.getInOutStore(year,type);

         //转换成Map<月份，数据>格式
         Map<Integer, Object> existMap = new HashMap<>();
         for (Map<String, Object> map : sum) {
             existMap.put((Integer) map.get("month"), map.get("y"));
         }

         //月份列表
         String[] months = new String[]{"", "一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"};

         //按1-12月封装数据，没有的月份置零
         List<Map<String, Object>> list = new ArrayList<>(12);
         for (int i = 1; i < 13; i++) {
             Map<String, Object> map = new HashMap<>();
             map.put("name", months[i]);
             map.put("y", existMap.get(i) == null ? 0 : existMap.get(i));
             list.add(map);
         }
         return list;
	}
	
    public void setReportDao(IReportDao reportDao) {
        this.reportDao = reportDao;
    }

	@Override
	public List returnOrders(Date date1,Date date2,String type) {
			return reportDao.returnOrders(date1,date2,type);
		
	}
}
