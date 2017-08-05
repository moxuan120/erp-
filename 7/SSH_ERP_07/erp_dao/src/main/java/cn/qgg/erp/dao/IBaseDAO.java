package cn.qgg.erp.dao;

import org.hibernate.criterion.DetachedCriteria;

import java.io.Serializable;
import java.util.List;

/**
 * Created by qgg on 2017/6/29.
 */
public interface IBaseDAO<T> {

    List<T> findAll();

    DetachedCriteria getCriteria(T t1, T t2, Object param);

    T get(Serializable id);

    void save(T entity);

    void update(T entity);

    void delete(Serializable id);

    void evict(T entity);

    List<T> findList(T t1, T t2, Object param);

    List<T> findPageList(T t1, T t2, Object param, int firstResult, int maxResult);

    Long findCount(T t1, T t2, Object param);

    String getName(Serializable uuid) throws IllegalAccessException;
}
