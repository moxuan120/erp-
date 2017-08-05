package cn.qgg.erp.dao.impl;

import cn.qgg.erp.dao.IOrderDetailDao;
import cn.qgg.erp.entity.Orderdetail;
import org.hibernate.criterion.DetachedCriteria;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class OrderDetailDaoTest {
    static IOrderDetailDao orderDetailDao;
    @BeforeClass
    public static void init(){

        ApplicationContext ac = new ClassPathXmlApplicationContext("classpath*:applicationContext_*.xml");
        orderDetailDao = (IOrderDetailDao)ac.getBean("orderDetailDao");

    }

    @Test
    public void test1(){
        DetachedCriteria criteria = DetachedCriteria.forClass(Orderdetail.class);
        Orderdetail orderdetail = orderDetailDao.get(9L);
    }
   @Test
    public void test2(){
       System.out.println();
    }
}