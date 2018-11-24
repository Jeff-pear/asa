package com.pears.asa.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.mail.javamail.JavaMailSender;

/**
 * @author: pears
 * @description: Sys
 * @date: 2018/11/23 10:18
 */
public interface RegisterService {
    /**
     * 注册创建用户
     *
     * @param jsonObject
     * @return
     */
    JSONObject createUser(JSONObject jsonObject);

}
