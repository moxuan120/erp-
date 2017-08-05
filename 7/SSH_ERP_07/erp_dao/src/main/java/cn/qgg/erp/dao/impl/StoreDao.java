package cn.qgg.erp.dao.impl;

import cn.qgg.erp.dao.IStoreDao;
import cn.qgg.erp.entity.Store;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

/**
 * 仓库
 */
public class StoreDao extends BaseDao<Store> implements IStoreDao{
    //分页条件

    @Override
    public DetachedCriteria getCriteria(Store t1, Store t2, Object param) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Store.class);
        if(t1!=null){
            //模糊查询
            if(StringUtils.isNotBlank(t1.getName())){
                criteria.add(Restrictions.like("name", t1.getName(), MatchMode.ANYWHERE));
            }
            //员工
            if(null != t1.getEmpuuid()){
                criteria.add(Restrictions.eq("empuuid", t1.getEmpuuid()));
            }
        }
        return criteria;
    }
}
