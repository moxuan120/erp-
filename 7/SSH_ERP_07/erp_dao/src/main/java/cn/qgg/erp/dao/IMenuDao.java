package cn.qgg.erp.dao;

import cn.qgg.erp.entity.Menu;

import java.util.List;

public interface IMenuDao extends IBaseDAO<Menu> {

    List<Menu> getMenus(Long uuid);
}
