package cn.qgg.erp.dao;

import java.util.List;

import cn.qgg.erp.entity.Emp;

public interface IEmpDao extends IBaseDAO<Emp> {

    Emp findUser(String username, String password);

    Emp findByUsername(String username);

    void alterPwd(long uuid, String newPwd);
    //查询部门下的所有员工
	List<Emp> findEmpDepList(String depUuid);
}
