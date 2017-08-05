package cn.qgg.erp.job;

import cn.qgg.erp.biz.IDailyReport;
import cn.qgg.erp.biz.IStoreAlertBiz;
import cn.qgg.erp.entity.StoreAlert;
import cn.qgg.erp.entity.Storeoper;
import cn.qgg.erp.exception.ErpException;
import cn.qgg.erp.utils.MailUtil;
import cn.qgg.erp.utils.TimeUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MailJob {
    private static final Logger log = LoggerFactory.getLogger(MailJob.class);
    private IStoreAlertBiz storeAlertBiz;
    private IDailyReport dailyReport;
    private MailUtil mailUtil;
    private String to;
    private Configuration freeMarker;

    public void sendStoreAlertMail() throws Exception{
        log.info("进入发送库存预警邮件任务");
        try {
            List<StoreAlert> storeAlertList = storeAlertBiz.getStoreAlertList();
            int count = storeAlertList.size();
            log.info("库存预警商品种类：" + count);
            if (count > 0) {
                String date = new SimpleDateFormat("yyyy年MM月dd日").format(new Date());
                //获取模板
                Template template = freeMarker.getTemplate("storeAlert.html");
                //填充数据
                Map<String, Object> model = new HashMap<String, Object>();
                //详情
                model.put("storeAlertList", storeAlertList);
                //时间
                ArrayList<String> dates = new ArrayList<>();
                dates.add(date);
                model.put("dates", dates);
                //把对象转化到模板里出,输出成字符串
                String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
                mailUtil.sendMail(to, "库存预警：" + date, content);
                log.info("库存预警邮件发送成功，邮箱：" + to);
            } else {
                throw new ErpException("没有库存预警的商品");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("库存预警邮件发送失败", e);
            throw e;
        }
    }

    /**
     * 每日报表
     *
     * @author MTCK
     */
    public void sendDailyReport()  throws Exception {
        log.info("进入定时邮件每日报表任务");
        try {
            //预警列表
            Storeoper storeoper1 = new Storeoper();
            storeoper1.setOpertime(TimeUtil.getBeforeDayStart(new Date()));
            Storeoper storeoper2 = new Storeoper();
            storeoper2.setOpertime(TimeUtil.getBeforeDayEnd(new Date()));
            List<Map<String, Object>> storeoperList = dailyReport.findList(storeoper1, storeoper2, null);
            List<Map<String, Object>> soReportList = dailyReport.storeoperReport(storeoper1, storeoper2, null);
            int count = storeoperList.size();
            log.info("每日报表商品种类：" + count);
            if (storeoperList.size() > 0) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
                String beforeDay = sdf.format(TimeUtil.getBeforeDayStart(new Date()));
                //获取模板
                Template template = freeMarker.getTemplate("storeoperReport.html");
                //填充数据
                Map<String, Object> model = new HashMap<String, Object>();
                //详情
                model.put("storeoperList", storeoperList);
                //统计
                model.put("soReportList", soReportList);
                //时间
                ArrayList<String> dates = new ArrayList<>();
                dates.add(beforeDay);
                model.put("dates", dates);
                //把对象转化到模板里出,输出成字符串
                String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
                mailUtil.sendMail(to, "每日报表：" + beforeDay, content);
                log.info("发送每日报表邮件成功! email:" + to);
            }
        } catch (Exception e) {
            log.error("发送每日报表邮件失败", e);
            e.printStackTrace();
            throw e;
        }
    }


    public void setStoreAlertBiz(IStoreAlertBiz storeAlertBiz) {
        this.storeAlertBiz = storeAlertBiz;
    }

    public void setMailUtil(MailUtil mailUtil) {
        this.mailUtil = mailUtil;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setFreeMarker(Configuration freeMarker) {
        this.freeMarker = freeMarker;
    }

    public void setDailyReport(IDailyReport dailyReport) {
        this.dailyReport = dailyReport;
    }

}
