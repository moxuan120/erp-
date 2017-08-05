package cn.qgg.erp.biz;

import cn.qgg.erp.dao.IBaseDAO;

import java.io.Serializable;
import java.util.List;

public interface IbaseBiz<T> {

    /**
     * @param t1 条件1
     * @param t2 条件2
     * @param param 其他参数
     * @param firstResult 起始值
     * @param maxResult 最大数量
     * @return
     */
    List<T> findPageList(T t1, T t2, Object param, int firstResult, int maxResult);

    List<T> findList(T t1, T t2, Object param);

    long findCount(T t1, T t2, Object param);

    void add(T t);

    void update(T t);

    T get(Serializable id);

    void evict(T t);

    void delete(Serializable id);

    String getName(Serializable uuid, IBaseDAO<?> dao);

}
