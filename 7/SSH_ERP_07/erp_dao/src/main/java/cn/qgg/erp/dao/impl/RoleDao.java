package cn.qgg.erp.dao.impl;

import cn.qgg.erp.dao.IRoleDao;
import cn.qgg.erp.entity.Role;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
/**
 * 角色数据
 *
 */
public class RoleDao extends BaseDao<Role> implements IRoleDao {

	@Override
	public DetachedCriteria getCriteria(Role t1,Role t2,Object param){
		DetachedCriteria criteria=DetachedCriteria.forClass(Role.class);
		if(t1!=null){
			//模糊查询
			if(StringUtils.isNotBlank(t1.getName())){
				criteria.add(Restrictions.like("name", t1.getName(), MatchMode.ANYWHERE));
			}
		}
		return criteria.addOrder(Order.asc("uuid"));
	}

}
