package cn.qgg.erp.biz.impl;

import cn.qgg.erp.biz.IDepBiz;
import cn.qgg.erp.dao.IDepDao;
import cn.qgg.erp.dao.IEmpDao;
import cn.qgg.erp.entity.Dep;
import cn.qgg.erp.entity.Emp;
import cn.qgg.erp.exception.ErpException;

import java.io.Serializable;
import java.util.List;

public class DepBiz extends BaseBiz<Dep> implements IDepBiz {
    private IDepDao depDao;
    private IEmpDao empDao;



    @Override
    public void delete(Serializable id) {
        Emp emp = new Emp();
        Dep dep = new Dep();
        dep.setUuid((Long) id);
        emp.setDep(dep);
        List<Emp> pageList = empDao.findPageList(emp, null, null, 0, 0);
        if(pageList!=null && pageList.size()>0) throw new ErpException("该部门下有员工");
        else super.delete(id);
    }
    public void setDepDao(IDepDao depDao) {
        this.depDao = depDao;
        super.setBaseDAO(depDao);
    }

    public void setEmpDao(IEmpDao empDao) {
        this.empDao = empDao;
    }
}
