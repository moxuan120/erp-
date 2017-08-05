package cn.qgg.erp.dao.impl;

import cn.qgg.erp.dao.IStoreAlertDao;
import cn.qgg.erp.entity.StoreAlert;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import java.util.List;

/**
 * 库存预警
 */
public class StoreAlertDao extends HibernateDaoSupport implements IStoreAlertDao {

    /**
     * 获取库存预警列表
     *
     * @return
     */
    @Override
    public List<StoreAlert> getStoreAlertList() {
        return (List<StoreAlert>) getHibernateTemplate().find("from StoreAlert where storenum < outnum");
    }
}
