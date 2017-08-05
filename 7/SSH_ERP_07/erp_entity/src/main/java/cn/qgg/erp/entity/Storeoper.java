package cn.qgg.erp.entity;

import org.apache.struts2.json.annotations.JSON;

import java.util.Date;

public class Storeoper {
	//采购入库
    public static final String TYPE_IN = "1";
    //销售出库
    public static final String TYPE_OUT = "2";
    //销售退货入库
    public static final String RETURN_TYPE_IN = "3";
    //采购退货出库
    public static final String RETURN_TYPE_OUT = "4";
    //盘盈入库
    public static final String INVENTORY_TYPE_IN = "5";
    //盘亏出库
    public static final String INVENTORY_TYPE_OUT = "6";
    private Long uuid;
    private Long empuuid;
    private String empName;
    private Date opertime;
    private Long storeuuid;
    private String storeName;
    private Long goodsuuid;
    private String goodsName;
    private Long num;
    private String type;

    public Long getUuid() {
        return uuid;
    }

    public void setUuid(Long uuid) {
        this.uuid = uuid;
    }

    public Long getEmpuuid() {
        return empuuid;
    }

    public void setEmpuuid(Long empuuid) {
        this.empuuid = empuuid;
    }

    @JSON(format = "yyyy-MM-dd：hh:mm")
    public Date getOpertime() {
        return opertime;
    }

    public void setOpertime(Date opertime) {
        this.opertime = opertime;
    }

    public Long getStoreuuid() {
        return storeuuid;
    }

    public void setStoreuuid(Long storeuuid) {
        this.storeuuid = storeuuid;
    }

    public Long getGoodsuuid() {
        return goodsuuid;
    }

    public void setGoodsuuid(Long goodsuuid) {
        this.goodsuuid = goodsuuid;
    }

    public Long getNum() {
        return num;
    }

    public void setNum(Long num) {
        this.num = num;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }
}
