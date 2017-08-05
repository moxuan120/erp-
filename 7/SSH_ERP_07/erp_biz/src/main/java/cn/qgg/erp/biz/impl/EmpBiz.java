package cn.qgg.erp.biz.impl;

import cn.qgg.erp.biz.IEmpBiz;
import cn.qgg.erp.dao.IDepDao;
import cn.qgg.erp.dao.IEmpDao;
import cn.qgg.erp.dao.IRoleDao;
import cn.qgg.erp.entity.Dep;
import cn.qgg.erp.entity.Emp;
import cn.qgg.erp.entity.Role;
import cn.qgg.erp.entity.Tree;
import cn.qgg.erp.exception.ErpException;
import cn.qgg.erp.utils.MailUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import redis.clients.jedis.Jedis;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmpBiz extends BaseBiz<Emp> implements IEmpBiz {
    private static final Logger log = LoggerFactory.getLogger(EmpBiz.class);
    private IEmpDao empDao;
    private IRoleDao roleDao;
    private IDepDao depDao;
    private MailUtil mailUtil;
    private String to;
    private Configuration freeMarker;

    public void setDepDao(IDepDao depDao) {
        this.depDao = depDao;
    }

    private Jedis jedis;

    /**
     * 根据username和password查找用户
     *
     * @param username
     * @param password
     * @return
     */
    @Override
    public Emp findUser(String username, String password) {
        return empDao.findUser(username, password);
    }

    /**
     * 查找是否存在用户名
     *
     * @param username
     * @return 返回是否存在此username
     */
    @Override
    public boolean checkName(String username) {
        Emp emp = empDao.findByUsername(username);
        return emp != null;
    }

    /**
     * 修改密码
     *
     * @param uuid   登陆用户ID
     * @param oldPwd 旧密码
     * @param newPwd 新密码
     */
    @Override
    public void alterPwd(long uuid, String oldPwd, String newPwd) {
        Emp emp = empDao.get(uuid);
        if (oldPwd.equals(emp.getPwd()))
            empDao.alterPwd(uuid, newPwd);
        else
            throw new ErpException("原密码输入不正确");
    }

    /**
     * 管理员重置密码
     *
     * @param uuid     用户id
     * @param resetPwd 重置的密码
     */
    @Override
    public void resetPwd(long uuid, String resetPwd) {
        Emp emp = empDao.get(uuid);
        resetPwd = new Md5Hash(resetPwd, emp.getUsername(), 2).toString();
        empDao.alterPwd(uuid, resetPwd);
    }

    /**
     * 获取用户角色
     *
     * @param uuid
     * @return
     */
    @Override
    public List<Tree> getRoles(Long uuid) {
        // 查出权限
        List<Role> roleList = roleDao.findAll();
        // 用户角色列表
        List<Role> empRoles = uuid == null ? new ArrayList<Role>() : empDao.get(uuid).getRoleList();
        // 构建权限树
        List<Tree> treeList = new ArrayList<>();
        Tree t1;
        for (Role role : roleList) {
            t1 = new Tree();
            t1.setId(String.valueOf(role.getUuid()));
            t1.setText(role.getName());
            if (empRoles.contains(role))
                t1.setChecked(true);// 选中拥有的角色
            treeList.add(t1);
        }
        if (uuid != null)
            log.info("获取【{}】用户的角色", empDao.get(uuid).getName());
        return treeList;
    }

    /**
     * 更新时保持关联信息
     *
     * @param emp
     */
    @Override
    public void update(Emp emp) {
        Emp emp1 = empDao.get(emp.getUuid());
        emp.setRoleList(emp1.getRoleList());// 保持角色信息
        evict(emp1);// 游离化，避免冲突
        super.update(emp);
    }

    /**
     * 更新用户角色
     *
     * @param uuid
     * @param checked
     */
    @Override
    public void updateRoles(Long uuid, String checked) {
        Emp emp = empDao.get(uuid);
        // 清空角色
        emp.setRoleList(new ArrayList<Role>());
        if (!checked.equals("")) {
            String[] ids = checked.split("-");
            for (String id : ids) {
                emp.getRoleList().add(roleDao.get(Long.valueOf(id)));
            }
        }
        log.info("更新了【{}】用户权限", emp.getName());
        jedis.del("empMenu" + uuid);
        log.info("清空了【{}】用户菜单redis缓存", emp.getName());
    }

    @Override
    public void add(Emp emp) {
        // 默认密码与用户名相同
        emp.setPwd(new Md5Hash(emp.getUsername(), emp.getUsername(), 2).toString());
        super.add(emp);
    }


    // 添加的代码 员工导出
    public void export(OutputStream os, Emp t1) {
        List<Emp> emplist = empDao.findList(t1, null, null);
        // 创建工作
        HSSFWorkbook wk = new HSSFWorkbook();
        HSSFSheet sheet = wk.createSheet("员工表");
        // 创建第一行 写入表头
        HSSFRow row = sheet.createRow(0);
        // 定义好每一列的标题
        String[] headernames = {"登录名", "真实姓名", "性别", "邮箱", "电话", "生日", "地址", "部门"};
        // 指定每一列的宽度
        int[] columnWidths = {4000, 4000, 2000, 5000, 4000, 4000, 6000, 4000};
        HSSFCell cell = null;
        for (int i = 0; i < headernames.length; i++) {
            cell = row.createCell(i);
            cell.setCellValue(headernames[i]);
            sheet.setColumnWidth(i, columnWidths[i]);

        }

        // 写入内容
        int i = 1;
        String gender = "";
        for (Emp e : emplist) {
            row = sheet.createRow(i);
            row.createCell(0).setCellValue(e.getUsername());
            row.createCell(1).setCellValue(e.getName());
            if (e.getGender() == 0) {
                gender = "女";
            } else {
                gender = "男";
            }
            row.createCell(2).setCellValue(gender);
            row.createCell(3).setCellValue(e.getEmail());
            row.createCell(4).setCellValue(e.getTele());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            row.createCell(5).setCellValue(sdf.format(e.getBirthday()));
            row.createCell(6).setCellValue(e.getAddress());
            row.createCell(7).setCellValue(e.getDep().getName());
            i++;
        }
        // 写入到输出流中
        try {
            wk.write(os);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                wk.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 员工的导入
    @SuppressWarnings("unused")
    @Override
    public int[] doImport(InputStream is) throws Exception {
        // 读取到re[0]条数据；更新re[1]条数据；新增re[2]条数据
        int[] re = new int[]{0, 0, 0};
        HSSFWorkbook wb = new HSSFWorkbook(is);
        // 获取第一个工作表
        HSSFSheet sheet = wb.getSheetAt(0);
        // 读取内容
        HSSFRow row = null;
        Emp emp = null;
        List<Emp> list = null;

        for (int i = 1; i <= sheet.getLastRowNum(); i++, re[0]++) {
            // 判断 是否 存在？名称
            row = sheet.getRow(i);
            emp = new Emp();
            // 获取名字
            emp.setUsername(row.getCell(0).getStringCellValue());
            list = super.findList(null, emp, null);
            if (list.size() > 0) {
                emp = list.get(0);
                re[1]++;
            }
            // 设置属性内容
            emp.setName(row.getCell(1).getStringCellValue());
            Long l;
            if (row.getCell(2).getStringCellValue().equals("女")) {
                l = 0L;
                emp.setGender(l);
            }
            if (row.getCell(2).getStringCellValue().equals("男")) {
                l = 1L;
                emp.setGender(l);
            }
            emp.setEmail(row.getCell(3).getStringCellValue());
            // 根据判断字段是什么类型 写入什么值
            if (row.getCell(4).getCellTypeEnum().equals(CellType.NUMERIC)) {
                String s = row.getCell(4).getNumericCellValue() + "";
                emp.setTele(s.substring(0, s.length() - 1).replaceAll("[\\D]", ""));
            } else {
                emp.setTele(row.getCell(4).getStringCellValue());
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
            emp.setBirthday(sdf.parse(row.getCell(5).getStringCellValue()));
            emp.setAddress(row.getCell(6).getStringCellValue());
            String depName = row.getCell(7).getStringCellValue();
            if (emp.getUuid() == null || !emp.getDep().getName().equals(depName)) {//修改depName
                Dep dep = new Dep();
                dep.setName(depName);
                List<Dep> findList = depDao.findList(null, dep, null);
                if (findList.size() > 0) {
                    emp.setDep(findList.get(0));
                } else {
                    throw new ErpException("部门不存在");
                }
            }
            if (emp.getUuid() == null) {//新建emp
                empDao.save(emp);
                re[2]++;
            }
        }
        wb.close();
        return re;
    }

    public void exportModel(ServletOutputStream os, Emp t1) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("员工");
        // 表头
        HSSFRow row0 = sheet.createRow(0);
        // 定义好每一列的标题
        String[] headernames = {"登录名", "真实姓名", "性别", "邮箱", "电话", "生日", "地址", "部门"};
        // 指定每一列的宽度
        int[] columnWidths = {4000, 4000, 2000, 5000, 4000, 4000, 6000, 4000};
        // 初始化表头
        HSSFCell cell = null;
        for (int i = 0; i < headernames.length; i++) {
            cell = row0.createCell(i);
            cell.setCellValue(headernames[i]);
            sheet.setColumnWidth(i, columnWidths[i]);
        }
        // 输出
        try {
            workbook.write(os);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    @Override
    //查询部门下所有员工
    public List<Emp> empDepList(String depUuid) {
        List<Emp> empList = empDao.findEmpDepList(depUuid);
        return empList;
    }

    @Override
    public void feedback(String to, String username, String fbSubjext, String fbText) throws Exception {
        log.info("发送反馈邮件");
        try {
            String date = new SimpleDateFormat("yyyy年MM月dd日 hh:mm:ss").format(new Date());
            //获取模板
            Template template = freeMarker.getTemplate("feedback.html");
            //填充数据
            Map<String, Object> model = new HashMap<String, Object>();
            //主题
            ArrayList<String> subjects = new ArrayList<>();
            subjects.add(fbSubjext);
            model.put("subjects", subjects);
            //用户
            ArrayList<String> users = new ArrayList<>();
            users.add(username);
            model.put("users", users);
            //正文
            ArrayList<String> texts = new ArrayList<>();
            texts.add(fbText);
            model.put("texts", texts);
            //把对象转化到模板里出,输出成字符串
            String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
            mailUtil.sendMail(to, "用户反馈：" + date, content);
            log.info("发送反馈邮件成功! email:" + to);
        } catch (Exception e) {
            log.error("发送反馈邮件", e);
            e.printStackTrace();
            throw e;
        }
    }

    public void setRoleDao(IRoleDao roleDao) {
        this.roleDao = roleDao;
    }

    public void setMailUtil(MailUtil mailUtil) {
        this.mailUtil = mailUtil;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setFreeMarker(Configuration freeMarker) {
        this.freeMarker = freeMarker;
    }

    public void setEmpDao(IEmpDao empDao) {
        this.empDao = empDao;
        super.setBaseDAO(empDao);
    }

    public void setJedis(Jedis jedis) {
        this.jedis = jedis;
    }
}
