package cn.qgg.erp.entity;

import java.io.Serializable;

public class RoleMenuPK implements Serializable {
    private Long roleuuid;
    private String menuuuid;

    public Long getRoleuuid() {
        return roleuuid;
    }

    public void setRoleuuid(Long roleuuid) {
        this.roleuuid = roleuuid;
    }

    public String getMenuuuid() {
        return menuuuid;
    }

    public void setMenuuuid(String menuuuid) {
        this.menuuuid = menuuuid;
    }


}
