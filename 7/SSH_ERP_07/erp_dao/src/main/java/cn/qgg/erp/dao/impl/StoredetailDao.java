package cn.qgg.erp.dao.impl;

import cn.qgg.erp.dao.IStoredetailDao;
import cn.qgg.erp.entity.Storedetail;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 * 仓库库存
 */
public class StoredetailDao extends BaseDao<Storedetail> implements IStoredetailDao {

    @Override
    public DetachedCriteria getCriteria(Storedetail t1, Storedetail t2, Object param) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Storedetail.class);
        if (t1 != null) {
            //商品
            if (null != t1.getGoodsuuid()) {
                criteria.add(Restrictions.eq("goodsuuid", t1.getGoodsuuid()));
            }
            //仓库
            if (null != t1.getStoreuuid()) {
                criteria.add(Restrictions.eq("storeuuid", t1.getStoreuuid()));
            }
            //大于数量
            if (null != t1.getNum()) {
				criteria.add(Restrictions.ge("num", t1.getNum()));
            }
        }
        if (t2!=null){
            //小于数量
            if (null != t2.getNum()) {
                criteria.add(Restrictions.le("num", t2.getNum()));
            }
        }
        return criteria.addOrder(Order.asc("goodsuuid"));
    }

}
