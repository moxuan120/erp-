package cn.qgg.erp.entity;

import org.apache.struts2.json.annotations.JSON;

import java.util.List;

public class Role {
    private Long uuid;
    private String name;
    private List<Menu> menuList;//菜单集合
    private List<Emp> empList;//员工集合

    public Long getUuid() {
        return uuid;
    }

    public void setUuid(Long uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JSON(serialize = false)
    public List<Menu> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<Menu> menuList) {
        this.menuList = menuList;
    }
    @JSON(serialize = false)
    public List<Emp> getEmpList() {
        return empList;
    }

    public void setEmpList(List<Emp> empList) {
        this.empList = empList;
    }
}
