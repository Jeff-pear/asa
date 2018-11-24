package com.pears.asa.util;

import com.pears.asa.util.model.MailVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.internet.MimeMessage;
import java.util.Properties;


public class EmailUtil {
    private static Logger logger = LoggerFactory.getLogger(EmailUtil.class);

    public static void addSendMailTask(final MailVO bean){

        try {
            logger.info(bean.toString());

            JavaMailSenderImpl senderImpl = new JavaMailSenderImpl();
            //设定mail server
            senderImpl.setHost("smtp.263.net");
            senderImpl.setUsername("zhuliye@sibs.com.cn") ;
            senderImpl.setPassword("zhu1987831");
            MimeMessage mailMessage = senderImpl.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage, true, "UTF-8");
            //发送者
            messageHelper.setFrom(bean.getFromUser());
            //接收者
            messageHelper.setTo(bean.getToUser());
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
        }catch (Exception e){
            logger.error(e.getMessage());
        }
    }
}
