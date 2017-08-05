package cn.qgg.erp.biz;

import cn.qgg.erp.entity.StoreAlert;
import cn.qgg.erp.exception.ErpException;

import javax.mail.MessagingException;
import java.util.List;

public interface IStoreAlertBiz{

    List<StoreAlert> getStoreAlertList();

    void sendStoreAlertMail() throws MessagingException,ErpException;
}
