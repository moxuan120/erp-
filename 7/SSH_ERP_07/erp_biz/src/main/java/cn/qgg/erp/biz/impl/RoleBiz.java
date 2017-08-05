package cn.qgg.erp.biz.impl;

import cn.qgg.erp.biz.IRoleBiz;
import cn.qgg.erp.dao.IMenuDao;
import cn.qgg.erp.dao.IRoleDao;
import cn.qgg.erp.dao.impl.MenuDao;
import cn.qgg.erp.entity.Emp;
import cn.qgg.erp.entity.Menu;
import cn.qgg.erp.entity.Role;
import cn.qgg.erp.entity.Tree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色业务逻辑类
 *
 * @author Administrator
 */
public class RoleBiz extends BaseBiz<Role> implements IRoleBiz {
    private static final Logger log = LoggerFactory.getLogger(RoleBiz.class);
    private IRoleDao roleDao;
    private IMenuDao menuDao;
    private Jedis jedis;

    /**
     * 更新时保持关联信息
     * @param role
     */
    @Override
    public void update(Role role) {
        Role role1 = roleDao.get(role.getUuid());
        role.setMenuList(role1.getMenuList());
        role.setEmpList(role1.getEmpList());
        evict(role1);
        super.update(role);
    }

    /**
     * 获取角色下的菜单
     *
     * @return
     */
    @Override
    public List<Tree> getRoleMenus(Long uuid) {
        //获取菜单
        Menu rootMenu = menuDao.get("0");
        //获取角色权限菜单
        List<Menu> menuList = uuid == null ? new ArrayList<Menu>() : roleDao.get(uuid).getMenuList();
        //转制tree树形结构
        List<Tree> treeList = new ArrayList<>();
        Tree t1;//一级菜单
        Tree t2;//二级菜单
        for (Menu m1 : rootMenu.getMenus()) {
            t1 = new Tree();
            t1.setId(m1.getMenuid());
            t1.setText(m1.getMenuname());
            for (Menu m2 : m1.getMenus()) {
                t2 = new Tree();
                t2.setId(m2.getMenuid());
                t2.setText(m2.getMenuname());
                if (menuList.contains(m2)) t2.setChecked(true);//选中拥有的权限
                t1.getChildren().add(t2);
            }
            treeList.add(t1);
        }
        if (uuid != null)log.info("获取【{}】角色的权限菜单",roleDao.get(uuid).getName());
        return treeList;
    }

    /**
     * 更新角色权限
     *
     * @param uuid
     * @param checked
     */
    @Override
    public void updateRoleMenus(Long uuid, String checked) {
        Role role = roleDao.get(uuid);
        role.setMenuList(new ArrayList<Menu>());//清空已有权限
        //更新权限
        String[] ids = checked.split("-");
        for (String id : ids) {
            role.getMenuList().add(menuDao.get(id));
        }
        log.info("更新了【{}】角色权限",role.getName());
        for (Emp emp : role.getEmpList()) {
            jedis.del("empMenu" + emp.getUuid());
        }
        log.info("清空了【{}】角色下用户的redis菜单缓存",role.getName());
    }

    public void setRoleDao(IRoleDao roleDao) {
        this.roleDao = roleDao;
        super.setBaseDAO(this.roleDao);
    }

    public void setMenuDao(MenuDao menuDao) {
        this.menuDao = menuDao;
    }

    public void setJedis(Jedis jedis) {
        this.jedis = jedis;
    }
}
