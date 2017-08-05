package cn.qgg.erp.action;

import cn.qgg.erp.biz.IEmpBiz;
import cn.qgg.erp.entity.Emp;
import cn.qgg.erp.exception.ErpException;
import cn.qgg.erp.utils.MailUtil;
import com.opensymphony.xwork2.ActionContext;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import redis.clients.jedis.Jedis;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UserAction extends BaseAction<Emp> {
    private IEmpBiz empBiz;
    private String username;//登陆名
    private String password;//密码
    private String oldPwd;
    private String newPwd;
    private String resetPwd;
    private Boolean rememberMe;
    private Emp user;
    private Jedis jedis;
    private MailUtil mailUtil;
    private String to;
    private String fbText;
    private String fbSubject;

    /**
     * @return 登入
     */
    public String login() {
        if (rememberMe == null) rememberMe = false;
        //UsernamePasswordToken token = new UsernamePasswordToken(username, password,rememberMe);
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        Subject subject = SecurityUtils.getSubject();
        try {
            System.out.println(password);
            subject.login(token);
            String loginTime = getLoginTime();
            ajaxReturn(true, "登陆成功" + loginTime);
        } catch (AuthenticationException e) {
            e.printStackTrace();
            ajaxReturn(false, "登陆失败");
        }
        return SUCCESS;
    }

    /**
     * @return 登入
     */
    public String login_old() {
        System.out.println(password);
        try {
            Emp user = empBiz.findUser(username, password);
            if (user != null) {
                ActionContext.getContext().getSession().put("user", user);
                ajaxReturn(true, "登陆成功");
            } else {
                if (empBiz.checkName(username)) ajaxReturn(false, "密码不匹配");
                else ajaxReturn(false, "用户名不存在");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ajaxReturn(false, "登陆失败");

        }
        return SUCCESS;
    }

    /**
     * @return 登出
     */
    public String logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        ajaxReturn(true, "退出成功");
        return SUCCESS;
    }

    /**
     * @return 获取登陆用户名
     */
    public String getName() {
        Subject subject = SecurityUtils.getSubject();
        Emp user = (Emp) subject.getPrincipal();
        if (user != null) ajaxReturn(true, user.getName());
        else ajaxReturn(false, "您没有登陆");
        return SUCCESS;
    }

    /**
     * 修改密码
     *
     * @return
     */
    public String alterPwd() {
        try {
            empBiz.alterPwd(getUser().getUuid(), oldPwd, newPwd);
            ajaxReturn(true, "密码修改成功");
        } catch (ErpException e) {
            e.printStackTrace();
            ajaxReturn(false, "密码修改失败：" + e.getMessage());
        }
        return SUCCESS;
    }

    /**
     * 管理员重置密码
     *
     * @return
     */
    public String resetPwd() {
        try {
            empBiz.resetPwd(super.getT1().getUuid(), resetPwd);
            ajaxReturn(true, "密码修改成功");
        } catch (ErpException e) {
            e.printStackTrace();
            ajaxReturn(false, "密码修改失败：" + e.getMessage());
        }
        return SUCCESS;
    }

    /**
     * 获取上次登录时间
     *
     * @return
     */
    public String getLoginTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String currentTime = sdf.format(new Date());

        String lastTime = jedis.get("lastTime" + getUser().getUuid());
        if (null == lastTime) {
            jedis.set("lastTime" + getUser().getUuid(), currentTime);
            return "你好,欢迎登录 这是您的第一次登录，欢迎光临";
        } else {
            jedis.set("lastTime" + getUser().getUuid(), currentTime);
            return "欢迎再次登录，您上次登录时间为：" + lastTime;
        }
    }

    /**
     * 反馈邮件
     *
     * @return
     */
    public String feedback() {
        Emp user = super.getUser();
        if (user == null) {
            throw new ErpException("没有登陆");
        }
        try {
            empBiz.feedback(to,user.getName(),  fbSubject,fbText);
            ajaxReturn(true, "反馈邮件发送成功");
        } catch (ErpException e) {
            e.printStackTrace();
            ajaxReturn(false, "反馈邮件发送失败：" + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            ajaxReturn(false, "反馈邮件发送失败");
        }
        return SUCCESS;
    }

    public void setEmpBiz(IEmpBiz empBiz) {
        this.empBiz = empBiz;
        super.setBaseBiz(empBiz);
    }

    public void setUsername(String username) {
        if (password != null) password = new Md5Hash(password, username, 2).toString();
        this.username = username;
    }

    public void setPassword(String password) {
        if (username != null) password = new Md5Hash(password, username, 2).toString();
        this.password = password;
    }

    public void setOldPwd(String oldPwd) {
        oldPwd = new Md5Hash(oldPwd, super.getUser().getUsername(), 2).toString();
        this.oldPwd = oldPwd;
    }

    public void setNewPwd(String newPwd) {
        newPwd = new Md5Hash(newPwd, super.getUser().getUsername(), 2).toString();
        this.newPwd = newPwd;
    }

    public void setResetPwd(String resetPwd) {
        this.resetPwd = resetPwd;
    }

    public void setRememberMe(Boolean rememberMe) {
        this.rememberMe = rememberMe;
    }

    public void setJedis(Jedis jedis) {
        this.jedis = jedis;
    }

    public void setMailUtil(MailUtil mailUtil) {
        this.mailUtil = mailUtil;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setFbText(String fbText) {
        this.fbText = fbText;
    }

    public void setFbSubject(String fbSubject) {
        this.fbSubject = fbSubject;
    }
}
