package cn.qgg.erp.entity;

import org.apache.struts2.json.annotations.JSON;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Orders {
    //订单的类型： 采购
    public static final String TYPE_IN = "1";
    //订单的状态： 未审核
    public static final String STATE_CREATE = "0";
    //订单的状态： 已审核
    public static final String STATE_CHECK = "1";
    //订单状态： 已确认
    public static final String STATE_START = "2";
    //订单状态： 已入库
    public static final String STATE_END = "3";
    

    //订单的类型： 销售
    public static final String TYPE_OUT = "2";
    //未出库
    public static final String STATE_NOT_OUT = "0";
    //已出库
    public static final String STATE_OUT = "1";

    //退货状态
    //订单状态： 未审核
    public static final String STATE_RETURN_CREATE = "4";
    //订单状态： 已审核
    public static final String STATE_RETURN_CHECK = "5";
    //订单状态： 已完成
    public static final String STATE_RETURN_END = "6";
    
    //部分退货
    //订单状态： 未审核
    public static final String STATE_RETURNDETAIL_CREATE = "7";
    //订单状态： 已审核
    public static final String STATE_RETURNDETAIL_CHECK = "8";
    //订单状态： 已完成
    public static final String STATE_RETURNDETAIL_END = "9";
    private Long uuid;
    private Date createtime;
    private Date checktime;
    private Date starttime;
    private Date endtime;
    private String type;
    private Long creater;
    private String createrName;
    private Long checker;
    private String checkerName;
    private Long starter;
    private String starterName;
    private Long ender;
    private String enderName;
    private Long supplieruuid;
    private String supplierName;
    private Double totalmoney;
    private String state;
    private Long waybillsn;
    private List<Orderdetail> orderdetails;

    public Long getUuid() {
        return uuid;
    }

    public void setUuid(Long uuid) {
        this.uuid = uuid;
    }

    @JSON(format = "yyyy-MM-dd：hh:mm")
    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    @JSON(format = "yyyy-MM-dd：hh:mm")
    public Date getChecktime() {
        return checktime;
    }

    public void setChecktime(Date checktime) {
        this.checktime = checktime;
    }

    @JSON(format = "yyyy-MM-dd：hh:mm")
    public Date getStarttime() {
        return starttime;
    }

    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    @JSON(format = "yyyy-MM-dd：hh:mm")
    public Date getEndtime() {
        return endtime;
    }

    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getCreater() {
        return creater;
    }

    public void setCreater(Long creater) {
        this.creater = creater;
    }

    public Long getChecker() {
        return checker;
    }

    public void setChecker(Long checker) {
        this.checker = checker;
    }

    public Long getStarter() {
        return starter;
    }

    public void setStarter(Long starter) {
        this.starter = starter;
    }

    public Long getEnder() {
        return ender;
    }

    public void setEnder(Long ender) {
        this.ender = ender;
    }

    public Long getSupplieruuid() {
        return supplieruuid;
    }

    public void setSupplieruuid(Long supplieruuid) {
        this.supplieruuid = supplieruuid;
    }

    public Double getTotalmoney() {
        return totalmoney;
    }

    public void setTotalmoney(Double totalmoney) {
        this.totalmoney = totalmoney;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Long getWaybillsn() {
        return waybillsn;
    }

    public void setWaybillsn(Long waybillsn) {
        this.waybillsn = waybillsn;
    }

    public List<Orderdetail> getOrderdetails() {
    	if (orderdetails == null) {
			return new ArrayList<Orderdetail>();
		}
        return orderdetails;
    }

    public void setOrderdetails(List<Orderdetail> orderdetails) {
        this.orderdetails = orderdetails;
    }

    public String getCreaterName() {
        return createrName;
    }

    public void setCreaterName(String createrName) {
        this.createrName = createrName;
    }

    public String getCheckerName() {
        return checkerName;
    }

    public void setCheckerName(String checkerName) {
        this.checkerName = checkerName;
    }

    public String getStarterName() {
        return starterName;
    }

    public void setStarterName(String starterName) {
        this.starterName = starterName;
    }

    public String getEnderName() {
        return enderName;
    }

    public void setEnderName(String enderName) {
        this.enderName = enderName;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }
}
