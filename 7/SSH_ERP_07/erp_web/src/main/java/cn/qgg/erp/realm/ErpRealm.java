package cn.qgg.erp.realm;

import cn.qgg.erp.biz.IEmpBiz;
import cn.qgg.erp.biz.IMenuBiz;
import cn.qgg.erp.entity.Emp;
import cn.qgg.erp.entity.Menu;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.util.List;

public class ErpRealm extends AuthorizingRealm {
    private static final Logger log = LoggerFactory.getLogger(ErpRealm.class);
    private IEmpBiz empBiz;
    private IMenuBiz menuBiz;
    private Jedis jedis;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        //获取权限
        Emp user = (Emp) principals.getPrimaryPrincipal();
        List<Menu> menus = menuBiz.getMenus(user.getUuid());

        //授予权限
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        for (Menu menu : menus) {
            info.addStringPermission(menu.getMenuname());
        }
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken token1 = (UsernamePasswordToken) token;
        log.info("登陆验证");
        String username = token1.getUsername();
        String password = new String(token1.getPassword());
        Emp user = empBiz.findUser(username, password);
        if (user == null) return null;
        log.info("用户【{}】验证成功",user.getName());
        return new SimpleAuthenticationInfo(user, password, getName());
    }

    public void setEmpBiz(IEmpBiz empBiz) {
        this.empBiz = empBiz;
    }

    public void setMenuBiz(IMenuBiz menuBiz) {
        this.menuBiz = menuBiz;
    }

    public void setJedis(Jedis jedis) {
        this.jedis = jedis;
    }
}
