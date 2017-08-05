package cn.qgg.erp.entity;

import org.apache.struts2.json.annotations.JSON;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

public class Returnorderdetail {
    //订单状态： 未出库/入库
    public static final String STATE_START = "0";
    //订单状态： 已出库/入库
    public static final String STATE_END = "1";
    private Long uuid;
    private Long goodsuuid;
    private String goodsname;
    private Double price;
    private Long num;
    private Double money;
    private Date endtime;
    private Long ender;
    private Long storeuuid;
    private String state;
//    private Long ordersuuid;
    @JSONField(serialize=false)
    private Returnorders returnorders;
    
    
    public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	public Returnorders getReturnorders() {
		return returnorders;
	}

	public void setReturnorders(Returnorders returnorders) {
		this.returnorders = returnorders;
	}

	public Long getUuid() {
        return uuid;
    }

    public void setUuid(Long uuid) {
        this.uuid = uuid;
    }

    public Long getGoodsuuid() {
        return goodsuuid;
    }

    public void setGoodsuuid(Long goodsuuid) {
        this.goodsuuid = goodsuuid;
    }

    public String getGoodsname() {
        return goodsname;
    }

    public void setGoodsname(String goodsname) {
        this.goodsname = goodsname;
    }

 
    public Long getNum() {
        return num;
    }

    public void setNum(Long num) {
        this.num = num;
    }


    @JSON(format = "yyyy-MM-dd")
    public Date getEndtime() {
        return endtime;
    }

    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    public Long getEnder() {
        return ender;
    }

    public void setEnder(Long ender) {
        this.ender = ender;
    }

    public Long getStoreuuid() {
        return storeuuid;
    }

    public void setStoreuuid(Long storeuuid) {
        this.storeuuid = storeuuid;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

}
