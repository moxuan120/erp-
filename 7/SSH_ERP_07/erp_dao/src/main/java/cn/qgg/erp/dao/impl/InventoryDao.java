package cn.qgg.erp.dao.impl;

import cn.qgg.erp.dao.IInventoryDao;
import cn.qgg.erp.entity.Inventory;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
/**
 * 盘盈盘亏
 *
 */
public class InventoryDao extends BaseDao<Inventory> implements IInventoryDao {

	@Override
	public DetachedCriteria getCriteria(Inventory t1,Inventory t2,Object param){
		DetachedCriteria criteria=DetachedCriteria.forClass(Inventory.class);
		if(t1!=null){
			//模糊查询
			if(StringUtils.isNotBlank(t1.getType())){
				criteria.add(Restrictions.eq("type", t1.getType()));
			}
			if(StringUtils.isNotBlank(t1.getRemark())){
				criteria.add(Restrictions.like("remark", t1.getRemark(), MatchMode.ANYWHERE));
			}
			//状态筛选
			if(StringUtils.isNotBlank(t1.getState())){
				criteria.add(Restrictions.eq("state", t1.getState()));
			}

		}
		return criteria.addOrder(Order.desc("uuid"));
	}

}
