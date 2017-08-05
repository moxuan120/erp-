package cn.qgg.erp.biz.impl;

import cn.qgg.erp.biz.IStoreAlertBiz;
import cn.qgg.erp.dao.IStoreAlertDao;
import cn.qgg.erp.entity.StoreAlert;
import cn.qgg.erp.exception.ErpException;
import cn.qgg.erp.utils.MailUtil;

import javax.mail.MessagingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class StoreAlertBiz implements IStoreAlertBiz {

    private IStoreAlertDao storeAlertDao;
    private MailUtil mailUtil;
    private String to;
    private String subject;
    private String text;

    /**
     * 获取库存预警列表
     *
     * @return
     */
    @Override
    public List<StoreAlert> getStoreAlertList() {
        return storeAlertDao.getStoreAlertList();
    }

    /**
     * 发送库存预警列表
     */
    @Override
    public void sendStoreAlertMail() throws MessagingException,ErpException{
        List<StoreAlert> storeAlertList = storeAlertDao.getStoreAlertList();
        int count = storeAlertList.size();
        if(count>0){
            String date = new SimpleDateFormat("yyyy-MM-dd : hh:mm:ss").format(new Date());
            mailUtil.sendMail(to,subject.replace("[time]",date),text.replace("[count]",String.valueOf(count)));
        }else {
            throw new ErpException("没有需要预警的商品");
        }
    }

    public void setMailUtil(MailUtil mailUtil) {
        this.mailUtil = mailUtil;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setStoreAlertDao(IStoreAlertDao storeAlertDao) {
        this.storeAlertDao = storeAlertDao;
    }
}
