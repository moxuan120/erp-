package cn.qgg.erp.dao.impl;

import cn.qgg.erp.dao.IBaseDAO;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by qgg on 2017/6/29.
 */
public class BaseDao<T> extends HibernateDaoSupport implements IBaseDAO<T> {
    //泛型的class对象
    private Class<T> entityClass;

    //在父类（BaseDao）的构造方法中动态获得entityClass
    public BaseDao() {
        ParameterizedType superclass = (ParameterizedType) this.getClass().getGenericSuperclass();
        //获得父类上声明的泛型数组
        Type[] actualTypeArguments = superclass.getActualTypeArguments();
        entityClass = (Class<T>) actualTypeArguments[0];
    }


    @Override
    public List<T> findAll() {
        return getHibernateTemplate().loadAll(entityClass);
    }

    //分页条件
    @Override
    public DetachedCriteria getCriteria(T t1, T t2, Object param) {
        return null;
    }

    @Override
    public T get(Serializable id) {
        return getHibernateTemplate().get(entityClass, id);
    }

    @Override
    public void save(T entity) {
        getHibernateTemplate().save(entity);
    }

    @Override
    public void update(T entity) {
        getHibernateTemplate().update(entity);
    }

    @Override
    public void delete(Serializable id) {
        T t = get(id);
        getHibernateTemplate().delete(t);
    }

    @Override
    public void evict(T entity){
        getHibernateTemplate().evict(entity);
    }

    @Override
    public List<T> findList(T t1, T t2, Object param) {
        DetachedCriteria criteria = getCriteria(t1, t2, param);
        return (List<T>) getHibernateTemplate().findByCriteria(criteria);
    }

    @Override
    public List<T> findPageList(T t1, T t2, Object param, int firstResult, int maxResult) {
        DetachedCriteria criteria = getCriteria(t1, t2, param);
        return (List<T>) getHibernateTemplate().findByCriteria(criteria, firstResult, maxResult);
    }

    @Override
    public Long findCount(T t1, T t2, Object param) {
        DetachedCriteria criteria = getCriteria(t1, t1, param);
        criteria.setProjection(Projections.rowCount());
        List<Long> list = (List<Long>) getHibernateTemplate().findByCriteria(criteria, 0, 0);
        return list.isEmpty() ? 0L : list.get(0);
    }

    @Override
    public String getName(Serializable uuid) throws IllegalAccessException {
        if (uuid != null) {
            T t = get(uuid);
            Field[] fields = entityClass.getDeclaredFields();
            for (Field field : fields) {
                if (field.getName().equals("name")) {
                    field.setAccessible(true);
                    return (String) field.get(t);
                }
            }
        }
        return null;
    }

}
