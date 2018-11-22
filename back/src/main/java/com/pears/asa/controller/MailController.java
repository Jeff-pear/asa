package com.pears.asa.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

@RestController
public class MailController {

    @Autowired
    JavaMailSender jms;

    @ApiOperation(value = "测试邮件",notes="注意事项")
    @RequestMapping(value = "/mail/test",method = RequestMethod.GET)
    public String send(){
        //建立邮件消息
        SimpleMailMessage mainMessage = new SimpleMailMessage();
        //发送者
        mainMessage.setFrom("zhuliye@sibs.com.cn");
        //接收者
        mainMessage.setTo("zhuliye@sibs.com.cn");
        //发送的标题
        mainMessage.setSubject("嗨喽");
        //发送的内容
        mainMessage.setText("hello world");
        jms.send(mainMessage);
        return "1";
    }
    @ApiOperation(value = "获取token",notes="注意事项")
    @ApiImplicitParam(name = "token",value = "用户id",dataType = "String",paramType = "path")
    @RequestMapping(value = "/user/info/{token}",method = RequestMethod.OPTIONS)
    public String testGet(@PathVariable String token){
        return token;
    }
}