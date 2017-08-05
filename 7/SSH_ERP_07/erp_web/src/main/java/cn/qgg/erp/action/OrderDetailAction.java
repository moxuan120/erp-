package cn.qgg.erp.action;

import cn.qgg.erp.biz.IOrderDetailBiz;
import cn.qgg.erp.entity.Emp;
import cn.qgg.erp.entity.Orderdetail;
import cn.qgg.erp.exception.ErpException;

public class OrderDetailAction extends BaseAction<Orderdetail> {

    private IOrderDetailBiz orderDetailBiz;
    private Long storeuuid;

    public String doInStore() {
        Emp user = super.getUser();
        if (user == null) {
            ajaxReturn(false, "您没有登陆");
            return SUCCESS;
        }
        if(storeuuid == null || getId() == null){
            ajaxReturn(false, "参数错误！");
            return SUCCESS;
        }
        try {
            orderDetailBiz.doInStore(getId(), user.getUuid(), storeuuid);
            ajaxReturn(true, "入库成功");
        } catch (ErpException e) {
            e.printStackTrace();
            ajaxReturn(false, "入库失败：" + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            ajaxReturn(false, "入库失败");
        }
        return SUCCESS;
    }

    public String doOutStore() {
        Emp user = super.getUser();
        if (user == null) {
            ajaxReturn(false, "您没有登陆");
            return SUCCESS;
        }
        if(storeuuid == null || getId() == null){
            ajaxReturn(false, "参数错误！");
            return SUCCESS;
        }
        try {
            orderDetailBiz.doOutStore(getId(), user.getUuid(), storeuuid);
            ajaxReturn(true, "出库成功");
        } catch (ErpException e) {
            e.printStackTrace();
            ajaxReturn(false, "出库失败：" + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            ajaxReturn(false, "出库失败");
        }
        return SUCCESS;
    }

    public void setOrderDetailBiz(IOrderDetailBiz orderDetailBiz) {
        this.orderDetailBiz = orderDetailBiz;
        super.setBaseBiz(orderDetailBiz);
    }

    public void setStoreuuid(Long storeuuid) {
        this.storeuuid = storeuuid;
    }
}
