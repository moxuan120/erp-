package cn.qgg.erp.action;

import cn.qgg.erp.biz.IOrdersBiz;
import cn.qgg.erp.entity.Emp;
import cn.qgg.erp.entity.Orderdetail;
import cn.qgg.erp.entity.Orders;
import cn.qgg.redsun.bos.ws.Waybilldetail;
import cn.qgg.redsun.bos.ws.impl.IWaybillWs;
import com.alibaba.fastjson.JSON;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class OrdersAction extends BaseAction<Orders> {

    private IOrdersBiz ordersBiz;
    private String json;
    private IWaybillWs waybillWs;
    private Long waybillSn;
    private List<Waybilldetail> waybilldetails;

    /**
     * 登陆账户的list
     *
     * @return
     * @throws IOException
     */
    public String myList() throws IOException {
        Orders orders = getT1();
        orders.setCreater(super.getUser().getUuid());
        return super.list();
    }

    /**
     * 审核订单
     *
     * @return
     */
    public String doCheck() {
        Emp user = super.getUser();
        if (user == null) {
            ajaxReturn(false, "您没有登陆");
            return SUCCESS;
        }
        try {
            ordersBiz.doCheck(super.getId(), user.getUuid());
            ajaxReturn(true, "审核成功");
        } catch (Exception e) {
            e.printStackTrace();
            ajaxReturn(false, "审核失败");
        }

        return SUCCESS;
    }

    /**
     * 确认订单
     *
     * @return
     */
    public String doStart() {
        Emp user = super.getUser();
        if (user == null) {
            ajaxReturn(false, "您没有登陆");
            return SUCCESS;
        }
        try {
            ordersBiz.doStart(super.getId(), user.getUuid());
            ajaxReturn(true, "确认成功");
        } catch (Exception e) {
            e.printStackTrace();
            ajaxReturn(false, "确认失败");
        }
        return SUCCESS;
    }

    /**
     * 添加订单
     * 覆写父类方法，级联保存Orderdetail
     *
     * @return
     * @throws Exception
     */
    @Override
    public String add() throws Exception {
        Emp user = super.getUser();
        if (user == null) {
            ajaxReturn(false, "请登陆后再操作");
            return SUCCESS;
        }
        try {
            Orders t1 = super.getT1();
            t1.setCreater(user.getUuid());
            List<Orderdetail> orderdetails = JSON.parseArray(json, Orderdetail.class);
            t1.setOrderdetails(orderdetails);
            super.add();
            ajaxReturn(true, "添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            ajaxReturn(false, "添加失败");
        }
        return SUCCESS;
    }

    /**
     * excel导出订单
     */
    public void exportExcel() {
        Orders orders = ordersBiz.get(getId());
        String name = orders.getType().equals(Orders.TYPE_IN) ? "采购单" : "销售单";
        String fileName = name + "_" + getId() + ".xls";
        HttpServletResponse response = ServletActionContext.getResponse();
        try {
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes(), "ISO-8859-1"));
            ordersBiz.exportExcel(response.getOutputStream(), getId());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 查询运单
     *
     * @return
     */
    public String waybillDetailList() {
        waybilldetails = waybillWs.waybillDetailList(waybillSn);
        return "waybilldetails";
    }


    public void setOrdersBiz(IOrdersBiz ordersBiz) {
        this.ordersBiz = ordersBiz;
        super.setBaseBiz(ordersBiz);
    }

    public void setJson(String json) {
        this.json = json;
    }

    public void setWaybillWs(IWaybillWs waybillWs) {
        this.waybillWs = waybillWs;
    }

    public void setWaybillSn(Long waybillSn) {
        this.waybillSn = waybillSn;
    }

    public List<Waybilldetail> getWaybilldetails() {
        return waybilldetails;
    }
}
