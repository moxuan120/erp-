package cn.qgg.erp.biz;

import cn.qgg.erp.entity.Menu;

import java.util.List;

public interface IMenuBiz extends IbaseBiz<Menu>{
    List<Menu> getMenus(Long uuid);

    Menu getRootTree(Long uuid);

    Menu getTree(Long uuid);
}
