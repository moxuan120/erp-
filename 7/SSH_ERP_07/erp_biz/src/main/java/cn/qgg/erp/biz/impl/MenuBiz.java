package cn.qgg.erp.biz.impl;

import cn.qgg.erp.biz.IMenuBiz;
import cn.qgg.erp.dao.IMenuDao;
import cn.qgg.erp.entity.Menu;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;

public class MenuBiz extends BaseBiz<Menu> implements IMenuBiz {
    private static final Logger log = LoggerFactory.getLogger(MenuBiz.class);
    private IMenuDao menuDao;
    private Jedis jedis;

    @Override
    public List<Menu> getMenus(Long uuid){
        List<Menu> menus;
        String empMenu;
        if((empMenu = jedis.get("empMenu"+uuid)) != null){
            menus = JSON.parseArray(empMenu,Menu.class);
            log.info("从redis缓存提取用户拥有权限的菜单");
        }else {
            menus = menuDao.getMenus(uuid);
            jedis.set("empMenu"+uuid, JSON.toJSONString(menus));
            log.info("从数据库查询用户拥有权限的菜单");
        }
        return menus;
    }

    @Override
    public Menu getRootTree(Long uuid) {
        Menu rootTree;
        String rootMenu;
        if((rootMenu = jedis.get("rootMenu")) != null){
            rootTree = JSON.parseObject(rootMenu,Menu.class);
            log.info("从redis缓存提取根菜单");
        }else {
            rootTree = menuDao.get("0");
            jedis.set("rootMenu", JSON.toJSONString(rootTree));
            log.info("从数据库查询根菜单");
        }
        return rootTree;
    }
    @Override
    public Menu getTree(Long uuid) {
        //取出用户拥有权限的菜单
        List<Menu> menus = getMenus(uuid);

        //利用根菜单转制树型
        Menu rootTree = getRootTree(uuid);
        List<Menu> ms1 = new ArrayList<>();
        for (Menu m1 : rootTree.getMenus()) {
            List<Menu> ms2 = new ArrayList<>();
            for (Menu m2 : m1.getMenus()) {
                for (Menu menu : menus) {
                    if (m2.getMenuid().equals(menu.getMenuid()))
                    ms2.add(m2);
                }
            }
            if (ms2.size() > 0) {
                m1.setMenus(ms2);
                ms1.add(m1);
            }
        }
        rootTree.setMenus(ms1);
        log.info("生成用户菜单树");
        return rootTree;
    }

    public void setMenuDao(IMenuDao menuDao) {
        this.menuDao = menuDao;
        super.setBaseDAO(menuDao);
    }

    public void setJedis(Jedis jedis) {
        this.jedis = jedis;
    }
}
