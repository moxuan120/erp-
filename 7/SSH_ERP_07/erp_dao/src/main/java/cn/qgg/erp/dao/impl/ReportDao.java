package cn.qgg.erp.dao.impl;

import cn.qgg.erp.dao.IReportDao;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 报表数据
 */
public class ReportDao extends HibernateDaoSupport implements IReportDao {
    @Override
    public List orderReport() {
        String sql = "select new map (t.name as name,sum(d.money) as y) " +
                "from GoodsType t,Goods g,Orders o,Orderdetail d " +
                "where g.goodsType = t and d.orders = o and d.goodsuuid = g.uuid and o.type = '2' " +
                "group by t.name";
        List<?> list = getHibernateTemplate().find(sql);
        return list;
    }

    @Override
    public List orderReport(Date date1, Date date2,String type) {
        if (date1 == null) date1 = new Date(0);
        if (date2 == null) date2 = new Date();
        String sql = "select new map (t.name as name,sum(d.money) as y) " +
                "from GoodsType t,Goods g,Orders o,Orderdetail d " +
                "where g.goodsType = t and d.orders = o and d.goodsuuid = g.uuid and o.type = ?" +
                "and o.createtime >= ? and o.createtime <= ? " +
                "group by t.name";
        return getHibernateTemplate().find(sql, type, date1, date2);
    }

    @Override
    public List<Map<String, Object>> getSumMoney(int year, String type) {
        String hql = "select new Map(month(o.createtime) as month, sum(od.money) as y) "
                + "from Orderdetail od, Orders o "
                + "where od.orders = o "
                + "and o.type= ? and od.state='1' "
                + "and year(o.createtime)=? "
                + "group by month(o.createtime)";
        return (List<Map<String, Object>>) this.getHibernateTemplate().find(hql, type,year);
    }

    @Override
	public List returnOrders(Date date1,Date date2,String type) {
		if (date1 == null) date1 = new Date(0);
		if (date2 == null) date2 = new Date();
		String sql = "select new map (t.name as name,sum(d.money) as y) " +
				"from GoodsType t,Goods g,Returnorders o,Returnorderdetail d " +
				"where g.goodsType = t and d.returnorders = o and d.goodsuuid = g.uuid and o.type = ?" +
				"and o.createtime >= ? and o.createtime <= ? " +
				"group by t.name";
		return getHibernateTemplate().find(sql, type, date1, date2);
	}

	@Override
	public List<Map<String, Object>> getSellMoney(int year,String type) {
		String hql = "select new Map(month(ro.endtime) as month, sum(rd.money) as y) "
				+ "from Returnorders ro, Returnorderdetail rd "
				+ "where rd.returnorders = ro "
				+ "and ro.state= '2' "
				+ "and ro.type= ? "
				+ "and year(ro.endtime)=? "
				+ "group by month(ro.endtime) ";
		return (List<Map<String, Object>>) this.getHibernateTemplate().find(hql, type, year);
	}
	//仓库进出货记录  type :1=进 / 2=出
	public List<Map<String, Object>> getInOutStore(int year,String type){
	String hql ="select new Map(month(so.opertime) as month,sum(so.num) as y) from Storeoper so "+
				"where year(so.opertime) = ? ";
				if("1".equals(type)){
				hql += "and (type = 1 or type =3 or type = 5) ";
				}
				if("2".equals(type)){
				hql += "and (type = 2 or type =4 or type = 6) ";
				}
				hql += "group by month(so.opertime)";
		return (List<Map<String, Object>>) this.getHibernateTemplate().find(hql,year);		
	}
}
