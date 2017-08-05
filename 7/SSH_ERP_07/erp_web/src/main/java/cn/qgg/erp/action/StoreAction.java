package cn.qgg.erp.action;

import cn.qgg.erp.biz.IStoreBiz;
import cn.qgg.erp.entity.Store;

import java.io.IOException;

public class StoreAction extends BaseAction<Store> {

    private IStoreBiz storeBiz;

    public String myList() throws IOException {
        Store store = new Store();
        store.setEmpuuid(super.getUser().getUuid());
        setT1(store);
        return super.simpleList();
    }

    public void setStoreBiz(IStoreBiz storeBiz) {
        this.storeBiz = storeBiz;
        super.setBaseBiz(storeBiz);
    }
}
