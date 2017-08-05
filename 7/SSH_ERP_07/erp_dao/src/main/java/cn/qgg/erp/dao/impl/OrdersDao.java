package cn.qgg.erp.dao.impl;

import cn.qgg.erp.dao.IOrdersDao;
import cn.qgg.erp.entity.Orders;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 * 订单
 */
public class OrdersDao extends BaseDao<Orders> implements IOrdersDao{
    //分页条件

    @Override
    public DetachedCriteria getCriteria(Orders t1, Orders t2, Object param) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Orders.class);
        if(t1!=null){
            //模糊查询
            if(StringUtils.isNotBlank(t1.getType())){
                criteria.add(Restrictions.like("type", t1.getType(), MatchMode.ANYWHERE));
            }
            if(StringUtils.isNotBlank(t1.getState())){
                criteria.add(Restrictions.like("state", t1.getState(), MatchMode.ANYWHERE));
            }
            //操作员
            if(null != t1.getCreater()){
                criteria.add(Restrictions.eq("creater", t1.getCreater()));
            }
        }
        return criteria.addOrder(Order.desc("uuid"));

    }
}
