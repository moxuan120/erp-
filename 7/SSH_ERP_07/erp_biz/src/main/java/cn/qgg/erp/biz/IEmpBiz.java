package cn.qgg.erp.biz;

import cn.qgg.erp.entity.Emp;
import cn.qgg.erp.entity.Tree;

import javax.servlet.ServletOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public interface IEmpBiz extends IbaseBiz<Emp>{

    Emp findUser(String username, String password);

    boolean checkName(String username);

    void alterPwd(long uuid, String oldPwd, String newPwd);

    void resetPwd(long uuid, String resetPwd);

    List<Tree> getRoles(Long uuid);

    void updateRoles(Long uuid, String checked);
    
    //员工的导出
    public  void  export(OutputStream os,Emp t1);
    
    //员工的导入
    public int[] doImport(InputStream is) throws  Exception;
    //下载模板
    void exportModel(ServletOutputStream os, Emp t1);
    //查询部门下所有员工
	List<Emp> empDepList(String depUuid);

    void feedback(String to, String username, String fbSubjext, String fbText) throws Exception;
}
