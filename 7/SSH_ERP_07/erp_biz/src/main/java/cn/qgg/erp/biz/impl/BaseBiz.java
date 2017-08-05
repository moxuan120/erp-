package cn.qgg.erp.biz.impl;

import cn.qgg.erp.biz.IbaseBiz;
import cn.qgg.erp.dao.IBaseDAO;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseBiz<T> implements IbaseBiz<T> {
    private IBaseDAO<T> baseDAO;
    private Map<String, String> map;//简单缓存

    @Override
    public List<T> findPageList(T t1, T t2, Object param, int firstResult, int maxResult) {
        return baseDAO.findPageList(t1, t2, param, firstResult, maxResult);
    }

    @Override
    public List<T> findList(T t1, T t2, Object param) {
        return baseDAO.findList(t1, t2, param);
    }

    @Override
    public long findCount(T t1, T t2, Object param) {
        return baseDAO.findCount(t1, t2, param);
    }

    @Override
    public void add(T t) {
        baseDAO.save(t);
    }

    @Override
    public void update(T t) {
        baseDAO.update(t);
        if (map != null) map.clear();//修改数据库清空缓存
    }

    @Override
    public T get(Serializable id) {
        return baseDAO.get(id);
    }

    /**
     * 转游离态
     * @param t
     */
    @Override
    public void evict(T t) {
        baseDAO.evict(t);
    }

    @Override
    public void delete(Serializable id) {
        baseDAO.delete(id);
        if (map != null) map.clear();//修改数据库清空缓存
    }


    /**
     * 通用通过id获取name
     * 利用map简单缓存
     *
     * @param uuid
     * @param dao
     * @return
     */
    @Override
    public String getName(Serializable uuid, IBaseDAO<?> dao) {
        if (null == uuid) return null;
        if (map == null) map = new HashMap<>();

        String daoName = dao.getClass().getSimpleName();
        String name = map.get(daoName + uuid);
        try {
            if (name == null) {
                name = dao.getName(uuid);
                map.put(daoName + uuid, name);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
        return name;
    }

    public void setBaseDAO(IBaseDAO<T> baseDAO) {
        this.baseDAO = baseDAO;
    }

}
