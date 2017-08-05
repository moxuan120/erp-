package cn.qgg.erp.filter;

import cn.qgg.erp.entity.Emp;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.Arrays;

public class ErpFilter extends AuthorizationFilter {
    private static final Logger log = LoggerFactory.getLogger(ErpFilter.class);

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        Subject subject = getSubject(request, response);
        Emp user = (Emp) subject.getPrincipal();
        String[] perms = (String[]) mappedValue;//权限列表
        if (user != null) {
            log.info("验证用户【{}】是否具有权限：{}", user.getName(), Arrays.toString(perms));
        } else {
            log.info("没有登陆的用户，登陆验证");
        }
        if (perms != null && perms.length > 0) {
            for (String perm : perms) {
                if (subject.isPermitted(perm)) {
                    log.info("授权成功");
                    return true;
                }
            }
        }
        log.info("授权失败");
        return false;
    }
}
