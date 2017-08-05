package cn.qgg.erp.dao.impl;

import cn.qgg.erp.dao.IDepDao;
import cn.qgg.erp.entity.Dep;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

public class BaseDaoTest {
    static IDepDao depDao;

    @BeforeClass
    public static void init() {
        ApplicationContext ac = new ClassPathXmlApplicationContext("classpath*:applicationContext_*.xml");
        depDao = (IDepDao) ac.getBean("depDao");
    }

    @Test
    public void loadAll() throws Exception {
        List<Dep> all = depDao.findAll();
        System.out.println(all);
    }


    @Test
    public void delete() throws Exception {
        depDao.delete(40L);
    }

    @Test
    public void getname() throws Exception {
        String name = depDao.getName(14L);
        System.out.println(name);
    }

}