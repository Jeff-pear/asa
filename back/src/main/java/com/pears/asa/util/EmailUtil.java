package com.pears.asa.util;

import com.pears.asa.config.properties.EmailConfig;
import com.pears.asa.util.model.MailVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.internet.MimeMessage;
import java.util.Properties;


public class EmailUtil {
    private static Logger logger = LoggerFactory.getLogger(EmailUtil.class);

    public static Boolean sendMailHtml(final MailVO bean, final EmailConfig emailConfig){

        try {
            logger.info(bean.toString());

            JavaMailSenderImpl senderImpl = new JavaMailSenderImpl();
            //设定mail server
            senderImpl.setHost(emailConfig.getHost());
            senderImpl.setUsername(emailConfig.getUsername()) ;
            senderImpl.setPassword(emailConfig.getPassword());
            MimeMessage mailMessage = senderImpl.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage, true, "UTF-8");
            //发送者
            messageHelper.setFrom(bean.getFromUser());
            //接收者
            messageHelper.setTo(bean.getToUser());
            //BCC
            messageHelper.setBcc(emailConfig.getBccuser();
            //发送的标题
            messageHelper.setSubject(bean.getSubject());
            //发送的内容
            messageHelper.setText(bean.getContent(),true);
            Properties prop = new Properties() ;
            prop.put("mail.smtp.auth", "true");// 将这个参数设为true，让服务器进行认证,认证用户名和密码是否正确
            prop.put("mail.smtp.timeout", "25000") ;
            senderImpl.setJavaMailProperties(prop);

            senderImpl.send(mailMessage);
            logger.info("邮件发送成功！");
            return true;
        }catch (Exception e){
            logger.error(e.getMessage());
            return false;
        }
    }
}
