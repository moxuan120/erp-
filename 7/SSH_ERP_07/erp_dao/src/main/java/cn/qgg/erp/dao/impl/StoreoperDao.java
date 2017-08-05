package cn.qgg.erp.dao.impl;

import cn.qgg.erp.dao.IStoreoperDao;
import cn.qgg.erp.entity.Storeoper;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
/**
 * 仓库操作记录
 *
 */
public class StoreoperDao extends BaseDao<Storeoper> implements IStoreoperDao {

	@Override
	public DetachedCriteria getCriteria(Storeoper t1,Storeoper t2,Object param){
		DetachedCriteria criteria=DetachedCriteria.forClass(Storeoper.class);
		if(t1!=null){
			//模糊查询
			if(StringUtils.isNotBlank(t1.getType())){
				criteria.add(Restrictions.like("type", t1.getType(), MatchMode.ANYWHERE));
			}
			//员工
			if (null != t1.getEmpuuid()){
				criteria.add(Restrictions.eq("empuuid",t1.getEmpuuid()));
			}
			//商品
			if (null != t1.getGoodsuuid()){
				criteria.add(Restrictions.eq("goodsuuid",t1.getGoodsuuid()));
			}
			//仓库
			if (null != t1.getStoreuuid()){
				criteria.add(Restrictions.eq("storeuuid",t1.getStoreuuid()));
			}
			//大于数量
			if (null != t1.getNum()) {
				criteria.add(Restrictions.ge("num", t1.getNum()));
			}
			//大于时间
			if (null != t1.getOpertime()) {
				criteria.add(Restrictions.ge("opertime", t1.getOpertime()));
			}
		}
		if (t2!=null){
			//小于数量
			if (null != t2.getNum()) {
				criteria.add(Restrictions.le("num", t2.getNum()));
			}
			//小于时间
			if (null != t2.getOpertime()) {
				criteria.add(Restrictions.le("opertime", t2.getOpertime()));
			}
		}
		return criteria.addOrder(Order.desc("uuid"));
	}

}
