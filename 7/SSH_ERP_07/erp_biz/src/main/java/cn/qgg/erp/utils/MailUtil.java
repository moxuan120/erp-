package cn.qgg.erp.utils;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * 发送邮件工具
 */
public class MailUtil {
    private JavaMailSender mailSender;//Sping邮件发送类
    private String from;//发件人


    /**
     * 发送邮件
     *
     * @param to      收件人
     * @param subject 主题
     * @param text    内容
     * @throws MessagingException
     */
    public void sendMail(String to, String subject, String text) throws MessagingException {
        //创建邮件
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        //邮件包装
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        helper.setFrom(from);
        helper.setSubject(subject);
        helper.setTo(to);
        helper.setText(text,true);
        mailSender.send(mimeMessage);
    }

    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}
