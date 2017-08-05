package cn.qgg.erp.biz.impl;

import cn.qgg.erp.biz.IDailyReport;
import cn.qgg.erp.biz.IStoreoperBiz;
import cn.qgg.erp.dao.IEmpDao;
import cn.qgg.erp.dao.IGoodsDao;
import cn.qgg.erp.dao.IStoreDao;
import cn.qgg.erp.entity.Storeoper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DailyReport implements IDailyReport {

	private IStoreoperBiz storeoperBiz;
	private IEmpDao empDao;
	private IGoodsDao goodsDao;
	private IStoreDao storeDao;

	/**
	 * 根据时间段找到每日报表
	 * 
	 */
	public List<Map<String, Object>> findList(Storeoper t1, Storeoper t2, Object param) {
		List<Map<String, Object>> reports = new ArrayList<>();
		List<Storeoper> list = storeoperBiz.findList(t1, t2, param);
		for (Storeoper storeoper : list) {
			Map<String, Object> report = new HashMap<String, Object>();
			report.put("goodsName", storeoper.getGoodsName());
			report.put("empName", storeoper.getEmpName());
			report.put("opertime", storeoper.getOpertime());
			report.put("storeName", storeoper.getStoreName());
			Long num = storeoper.getNum();
			report.put("num", num);
			String type = storeoper.getType();
			report.put("type", typeConvert(type));
			if (type.equals("2") || type.equals("3")) {
				Double outprice = goodsDao.get(storeoper.getGoodsuuid()).getOutprice();
				report.put("price", outprice);
				report.put("totalMoney", outprice * num);
			}
			if (type.equals("1") || type.equals("4") || type.equals("5")|| type.equals("6")) {
				Double inprice = goodsDao.get(storeoper.getGoodsuuid()).getInprice();
				report.put("price", inprice);
				report.put("totalMoney", inprice * num);
			}
			reports.add(report);
		}
		
		return reports;
	}

	@Override
	public List<Map<String, Object>>  storeoperReport(Storeoper t1, Storeoper t2, Object param){
		List<Storeoper> list = storeoperBiz.findList(t1, t2, param);
		ArrayList<Map<String, Object>> maps = new ArrayList<>();
		Map<String, Object> map = new HashMap<String, Object>();
		for (Storeoper storeoper : list) {
			String type = storeoper.getType();
			Double totalMoney = (Double) map.get(type);
			if(totalMoney == null) totalMoney = 0.0;
			if (type.equals("2") || type.equals("3")) {
				map.put(type, totalMoney + goodsDao.get(storeoper.getGoodsuuid()).getOutprice() * storeoper.getNum());
			}
			if (type.equals("1") || type.equals("4") || type.equals("5")|| type.equals("6")) {
				map.put(type, totalMoney + goodsDao.get(storeoper.getGoodsuuid()).getInprice() * storeoper.getNum());
			}
		}
		for (String key : map.keySet()) {
			HashMap<String, Object> returnMap = new HashMap<>();
			returnMap.put("type",typeConvert(key));
			returnMap.put("totalMoney",map.get(key));
			maps.add(returnMap);
		}
		return maps;
	}

	private String typeConvert(String type){
		switch (type){
			case "1": return "采购入库";
			case "2": return "销售出库";
			case "3": return "销售退货入库";
			case "4": return "采购退货出库";
			case "5": return "盘盈入库";
			case "6": return "盘亏出库";
			default: return "";
		}
	}

	public void setStoreoperBiz(IStoreoperBiz storeoperBiz) {
		
		this.storeoperBiz = storeoperBiz;
	}

	public void setEmpDao(IEmpDao empDao) {
		this.empDao = empDao;
	}

	public void setGoodsDao(IGoodsDao goodsDao) {
		this.goodsDao = goodsDao;
	}

	public void setStoreDao(IStoreDao storeDao) {
		this.storeDao = storeDao;
	}

}
