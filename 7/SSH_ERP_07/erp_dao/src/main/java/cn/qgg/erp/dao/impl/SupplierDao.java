package cn.qgg.erp.dao.impl;

import cn.qgg.erp.dao.ISupplierDao;
import cn.qgg.erp.entity.Supplier;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 * 供应商/客户
 */
public class SupplierDao extends BaseDao<Supplier> implements ISupplierDao {

    @Override
    public DetachedCriteria getCriteria(Supplier t1, Supplier t2, Object param) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Supplier.class);
        if (t1 != null) {
            //模糊查询
            if (StringUtils.isNotBlank(t1.getName())) {
                criteria.add(Restrictions.like("name", t1.getName(), MatchMode.ANYWHERE));
            }
            if (StringUtils.isNotBlank(t1.getAddress())) {
                criteria.add(Restrictions.like("address", t1.getAddress(), MatchMode.ANYWHERE));
            }
            if (StringUtils.isNotBlank(t1.getContact())) {
                criteria.add(Restrictions.like("contact", t1.getContact(), MatchMode.ANYWHERE));
            }
            if (StringUtils.isNotBlank(t1.getTele())) {
                criteria.add(Restrictions.like("tele", t1.getTele(), MatchMode.ANYWHERE));
            }
            if (StringUtils.isNotBlank(t1.getEmail())) {
                criteria.add(Restrictions.like("email", t1.getEmail(), MatchMode.ANYWHERE));
            }
            //类型（1：供应商/2：客户）
            if (StringUtils.isNotBlank(t1.getType())) {
                criteria.add(Restrictions.eq("type", t1.getType()));
            }

        }
        if (t2 != null) {
            //精确匹配
            if (StringUtils.isNotBlank(t2.getName())) {
                criteria.add(Restrictions.eq("name", t2.getName()));
            }
        }
        return criteria.addOrder(Order.asc("uuid"));
    }

}
