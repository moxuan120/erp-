package cn.qgg.erp.interceptor;

import cn.qgg.erp.entity.Emp;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;
import org.apache.struts2.ServletActionContext;

/**
 * Created by qgg on 2017/6/19.
 */
public class AutoLoginInterceptor extends MethodFilterInterceptor {


    @Override
    protected String doIntercept(ActionInvocation invocation) throws Exception {
        System.out.println("AutoLogin");
        Emp loginUser = (Emp) ServletActionContext.getRequest().getSession().getAttribute("user");
        return loginUser == null? "login" : invocation.invoke();
    }
}
