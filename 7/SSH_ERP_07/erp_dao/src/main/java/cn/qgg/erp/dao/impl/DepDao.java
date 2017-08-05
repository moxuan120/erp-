package cn.qgg.erp.dao.impl;

import cn.qgg.erp.dao.IDepDao;
import cn.qgg.erp.entity.Dep;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 * 部门
 */
public class DepDao extends BaseDao<Dep> implements IDepDao {
	// 分页条件

	@Override
	public DetachedCriteria getCriteria(Dep t1, Dep t2, Object param) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Dep.class);
		if (t1 != null) {
			// 模糊查询
			if (StringUtils.isNotBlank(t1.getName()))
				criteria.add(Restrictions.like("name", t1.getName(), MatchMode.ANYWHERE));
			if (StringUtils.isNotBlank(t1.getTele()))
				criteria.add(Restrictions.like("tele", t1.getTele(), MatchMode.ANYWHERE));
		}
		if (t2 != null) {
			if (StringUtils.isNotBlank(t2.getName()))
				criteria.add(Restrictions.eq("name", t2.getName()));
		}
		return criteria.addOrder(Order.asc("uuid"));
	}
}
