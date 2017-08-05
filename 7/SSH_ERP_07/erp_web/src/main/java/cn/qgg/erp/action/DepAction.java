package cn.qgg.erp.action;

import cn.qgg.erp.biz.IDepBiz;
import cn.qgg.erp.entity.Dep;

public class DepAction extends BaseAction<Dep> {

    private IDepBiz depBiz;

    public void setDepBiz(IDepBiz depBiz) {
        this.depBiz = depBiz;
        super.setBaseBiz(depBiz);
    }

}
