package cn.qgg.erp.action;

import cn.qgg.erp.biz.IMenuBiz;
import cn.qgg.erp.entity.Menu;

public class MenuAction extends BaseAction<Menu> {
    private String id;
    private IMenuBiz menuBiz;
    private Menu menuTree;

    /**
     * 获取菜单树数据
     * @return
     * @throws Exception
     */
    public String getTree() throws Exception {
        menuTree = menuBiz.getTree(getUser().getUuid());
        //menuTree = menuBiz.get("0");
        return "menuTree";
    }
    public void setId(String id) {
        this.id = id;
    }

    public void setMenuBiz(IMenuBiz menuBiz) {
        this.menuBiz = menuBiz;
        super.setBaseBiz(menuBiz);
    }

    public Menu getMenuTree() {
        return menuTree;
    }
}
