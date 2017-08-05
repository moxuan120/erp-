package cn.qgg.erp.dao.impl;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import cn.qgg.erp.dao.IReturnordersDao;
import cn.qgg.erp.entity.Returnorders;
/**
 * 退货订单
 *
 */
public class ReturnordersDao extends BaseDao<Returnorders> implements IReturnordersDao {

	@Override
	public DetachedCriteria getCriteria(Returnorders t1,Returnorders t2,Object param){
		DetachedCriteria criteria=DetachedCriteria.forClass(Returnorders.class);
		if(t1!=null){
			//模糊查询
			if(StringUtils.isNotBlank(t1.getType())){
				criteria.add(Restrictions.like("type", t1.getType(), MatchMode.ANYWHERE));
			}
			//库管员
			if(null != t1.getState()){
				criteria.add(Restrictions.eq("state", t1.getState()));
			}
			if(StringUtils.isNotBlank(t1.getState())){
                criteria.add(Restrictions.eq("state", t1.getState()));
            }

		}
		return criteria;
	}

}
