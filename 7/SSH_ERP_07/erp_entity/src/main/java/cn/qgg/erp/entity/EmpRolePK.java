package cn.qgg.erp.entity;

import java.io.Serializable;

public class EmpRolePK implements Serializable {
    private Long empuuid;
    private Long roleuuid;

    public Long getEmpuuid() {
        return empuuid;
    }

    public void setEmpuuid(Long empuuid) {
        this.empuuid = empuuid;
    }

    public Long getRoleuuid() {
        return roleuuid;
    }

    public void setRoleuuid(Long roleuuid) {
        this.roleuuid = roleuuid;
    }


}
