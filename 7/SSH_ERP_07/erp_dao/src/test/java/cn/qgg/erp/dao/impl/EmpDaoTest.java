package cn.qgg.erp.dao.impl;

import cn.qgg.erp.dao.IEmpDao;
import cn.qgg.erp.entity.Dep;
import cn.qgg.erp.entity.Emp;
import org.hibernate.criterion.DetachedCriteria;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

public class EmpDaoTest {
    static IEmpDao empDao;
    @BeforeClass
    public static void init(){

        ApplicationContext ac = new ClassPathXmlApplicationContext("classpath*:applicationContext_*.xml");
        empDao = (IEmpDao)ac.getBean("empDao");

    }

    @Test
    public void test1(){
        DetachedCriteria criteria = DetachedCriteria.forClass(Emp.class);
        List<Emp> pageList = empDao.findPageList(null, null, null, 0, 10);
        List<Emp> all = empDao.findAll();
        System.out.println(pageList);
        System.out.println(all);
    }
    @Test
    public void test2(){
        System.out.println(Dep.class.getSimpleName().toLowerCase());
    }
}