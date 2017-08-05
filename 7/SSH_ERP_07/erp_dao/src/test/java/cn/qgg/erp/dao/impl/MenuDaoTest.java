package cn.qgg.erp.dao.impl;

import cn.qgg.erp.dao.IMenuDao;
import cn.qgg.erp.entity.Menu;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

public class MenuDaoTest {
    static IMenuDao menuDao;

    @BeforeClass
    public static void init() {
        ApplicationContext ac = new ClassPathXmlApplicationContext("classpath*:applicationContext_*.xml");
        menuDao = (IMenuDao) ac.getBean("menuDao");
    }
    @Test
    public void getMenus() throws Exception {
        List<Menu> menus = menuDao.getMenus(3L);
        for (Menu menu : menus) {
            System.out.println(menu.getMenuname());
        }
    }

}