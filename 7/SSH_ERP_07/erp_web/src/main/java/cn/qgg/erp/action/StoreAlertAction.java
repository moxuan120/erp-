package cn.qgg.erp.action;

import cn.qgg.erp.biz.IStoreAlertBiz;
import cn.qgg.erp.entity.StoreAlert;
import cn.qgg.erp.exception.ErpException;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StoreAlertAction {
    private static final String SUCCESS = "success";
    private IStoreAlertBiz storeAlertBiz;
    private Map<String, Object> jsonResult = new HashMap<>(); //json返回数据

    public String list() throws IOException {
        List<StoreAlert> list = storeAlertBiz.getStoreAlertList();
        jsonResult.put("rows", list);
        jsonResult.put("total", list.size());
        return SUCCESS;
    }

    public String  sendStoreAlertMail(){
        try {
            storeAlertBiz.sendStoreAlertMail();
            ajaxReturn(true,"预警发送成功");
        } catch (MessagingException e) {
            e.printStackTrace();
            ajaxReturn(false,"邮件发送失败");
        } catch (ErpException e){
            e.printStackTrace();
            ajaxReturn(false,"预警发送失败：" + e.getMessage() );
        } catch (Exception e){
            e.printStackTrace();
            ajaxReturn(false,"预警发送失败");
        }
        return SUCCESS;
    }

    /**
     * @param success ：操作是否成功
     * @param message ：回显的信息
     *                ajax回调参数，用于回显信息
     */
    protected void ajaxReturn(boolean success, String message) {
        jsonResult.put("success", success);
        jsonResult.put("message", message);
    }
    public void setStoreAlertBiz(IStoreAlertBiz storeAlertBiz) {
        this.storeAlertBiz = storeAlertBiz;
    }

    public Map<String, Object> getJsonResult() {
        return jsonResult;
    }
}
