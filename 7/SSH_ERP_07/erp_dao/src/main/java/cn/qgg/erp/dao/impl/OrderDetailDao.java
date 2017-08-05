package cn.qgg.erp.dao.impl;

import cn.qgg.erp.dao.IOrderDetailDao;
import cn.qgg.erp.entity.Orderdetail;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 * 订单详情（子项）
 */
public class OrderDetailDao extends BaseDao<Orderdetail> implements IOrderDetailDao{
    //分页条件

    @Override
    public DetachedCriteria getCriteria(Orderdetail t1, Orderdetail t2, Object param) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Orderdetail.class);
        if(t1!=null){
            //模糊查询
            if(StringUtils.isNotBlank(t1.getGoodsname())){
                criteria.add(Restrictions.like("goodsname", t1.getGoodsname(), MatchMode.ANYWHERE));
            }
            if(StringUtils.isNotBlank(t1.getState())){
                criteria.add(Restrictions.eq("state", t1.getState()));
            }

            //根据商品类型查询
            if(null != t1.getOrders() && null != t1.getOrders().getUuid()){
                criteria.add(Restrictions.eq("orders", t1.getOrders()));
            }
        }
        return criteria.addOrder(Order.desc("uuid"));

    }
}
