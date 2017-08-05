package cn.qgg.erp.biz.impl;

import cn.qgg.erp.biz.IStoreBiz;
import cn.qgg.erp.dao.IStoreDao;
import cn.qgg.erp.entity.Store;

public class StoreBiz extends BaseBiz<Store> implements IStoreBiz {
    private IStoreDao storeDao;

    public void setStoreDao(IStoreDao storeDao) {
        this.storeDao = storeDao;
        super.setBaseDAO(storeDao);
    }
}
