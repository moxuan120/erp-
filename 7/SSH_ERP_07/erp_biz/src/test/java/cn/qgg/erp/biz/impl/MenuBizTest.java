package cn.qgg.erp.biz.impl;

import cn.qgg.erp.biz.IMenuBiz;
import cn.qgg.erp.entity.Menu;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MenuBizTest {
    static IMenuBiz menuBiz;

    @BeforeClass
    public static void init() {
        ApplicationContext ac = new ClassPathXmlApplicationContext("classpath*:applicationContext_*.xml");
        menuBiz = (IMenuBiz) ac.getBean("menuBiz");
    }
    @Test
    public void getTree() throws Exception {
        Menu tree = menuBiz.getTree(3L);
        System.out.println(tree);
    }

}