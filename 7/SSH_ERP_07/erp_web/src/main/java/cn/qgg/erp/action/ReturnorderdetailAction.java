package cn.qgg.erp.action;
import cn.qgg.erp.biz.IReturnorderdetailBiz;
import cn.qgg.erp.entity.Emp;
import cn.qgg.erp.entity.Returnorderdetail;
import cn.qgg.erp.exception.ErpException;

/**
 * 退货订单明细Action 
 * @author Administrator
 *
 */
public class ReturnorderdetailAction extends BaseAction<Returnorderdetail> {

	private IReturnorderdetailBiz returnorderdetailBiz;
	
	private Long storeuuid;
	/**
	 * 销售退货
	 * 
	 * @return
	 */
	public String doReturnInStore() {
		Emp user = super.getUser();
		if (user == null) {
			ajaxReturn(false, "您没有登陆");
			return SUCCESS;
		}
		if (storeuuid == null || getId() == null) {
			ajaxReturn(false, "参数错误！");
			return SUCCESS;
		}
		try {
			returnorderdetailBiz.doReturnInStore(getId(), user.getUuid(), storeuuid);
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
	
	/**
	 * 采购退货
	 * @return
	 */
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
        	returnorderdetailBiz.doOutStore(getId(), user.getUuid(), storeuuid);
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

	public void setReturnorderdetailBiz(IReturnorderdetailBiz returnorderdetailBiz) {
		this.returnorderdetailBiz = returnorderdetailBiz;
		super.setBaseBiz(this.returnorderdetailBiz);
	}

	public void setStoreuuid(Long storeuuid) {
		this.storeuuid = storeuuid;
	}

}
