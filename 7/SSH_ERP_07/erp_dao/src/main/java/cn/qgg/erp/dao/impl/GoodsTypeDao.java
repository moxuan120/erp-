package cn.qgg.erp.dao.impl;

import cn.qgg.erp.dao.IGoodsTypeDao;
import cn.qgg.erp.entity.GoodsType;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 * 商品类型
 */
public class GoodsTypeDao extends BaseDao<GoodsType> implements IGoodsTypeDao{
    //分页条件
    @Override
    public DetachedCriteria getCriteria(GoodsType t1, GoodsType t2, Object param) {
        DetachedCriteria criteria = DetachedCriteria.forClass(GoodsType.class);
        if(t1 != null){
            //模糊查询
            if(StringUtils.isNotBlank(t1.getName()))
                criteria.add(Restrictions.like("name", t1.getName(), MatchMode.ANYWHERE));
        }
        if (t2 != null) {
			if (StringUtils.isNotBlank(t2.getName()))
				criteria.add(Restrictions.eq("name", t2.getName()));
		}
        return criteria.addOrder(Order.desc("uuid"));

    }
}
