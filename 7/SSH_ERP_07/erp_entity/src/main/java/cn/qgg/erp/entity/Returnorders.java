package cn.qgg.erp.entity;

import org.apache.struts2.json.annotations.JSON;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Returnorders {
    //订单类型： 采购
    public static final String TYPE_IN = "1";
    //订单类型： 销售
    public static final String TYPE_OUT = "2";

    //订单状态： 未审核
    public static final String STATE_CREATE = "0";
    //订单状态： 已审核
    public static final String STATE_CHECK = "1";
    //订单状态： 已完成
    public static final String STATE_END = "2";
    private Long uuid;
    private Date createtime;
    private Date checktime;
    private Date endtime;
    private String type;
    private Long creater;
    private String createrName;//下单员名称
    private Long checker;
    private String checkerName;//审核员名称
    private Long ender;
    private String enderName;//库管员名称
    private Long supplieruuid;
    private String supplierName;//供应商或客户名称
    private Double totalmoney;
    private String state;
    private Long waybillsn;
//    private Long ordersuuid;//原订单编号
    private Orders orders;
    
    
   public Double getTotalmoney() {
		return totalmoney;
	}

	public void setTotalmoney(Double totalmoney) {
		this.totalmoney = totalmoney;
	}

private List<Returnorderdetail> returnorderdetails;
    
    

    public List<Returnorderdetail> getReturnorderdetails() {
    	if (returnorderdetails == null) {
			return new ArrayList<Returnorderdetail>();
		}
		return returnorderdetails;
	}

	public void setReturnorderdetails(List<Returnorderdetail> returnorderdetails) {
		this.returnorderdetails = returnorderdetails;
	}

	public Orders getOrders() {
		return orders;
	}

	public void setOrders(Orders orders) {
		this.orders = orders;
	}

	public Long getUuid() {
        return uuid;
    }

    public void setUuid(Long uuid) {
        this.uuid = uuid;
    }
    @JSON(format = "yyyy-MM-dd")
    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }
    @JSON(format = "yyyy-MM-dd")
    public Date getChecktime() {
        return checktime;
    }

    public void setChecktime(Date checktime) {
        this.checktime = checktime;
    }
    @JSON(format = "yyyy-MM-dd")
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
