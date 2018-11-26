package com.pears.asa.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.pears.asa.config.properties.EmailConfig;
import com.pears.asa.dao.UserDao;
import com.pears.asa.service.RegisterService;
import com.pears.asa.util.CommonUtil;
import com.pears.asa.util.EmailUtil;
import com.pears.asa.util.StringTools;
import com.pears.asa.util.constants.Constants;
import com.pears.asa.util.model.MailVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: pears
 * @description: 注册
 * @date: 2017/11/24 10:18
 */
@Service
public class RegisterServiceImpl implements RegisterService {
    @Autowired
    private UserDao userDao;
    @Autowired
    EmailConfig emailConfig;

    private static Logger logger = LoggerFactory.getLogger(RegisterServiceImpl.class);
    /**
     * 注册用户
     *
     * @param jsonObject
     * @return
     */
    @Override
    public JSONObject createUser(JSONObject jsonObject) {
        CommonUtil.fillPageParam(jsonObject);
        String curUserType = jsonObject.getString("curUserType");
        if(curUserType.equalsIgnoreCase(Constants.STUDENT_GROUP)){
            jsonObject.put("activeStatus",Constants.USER_ACTIVE);
        }
        int result = userDao.registerUser(jsonObject);
        //send email
        MailVO vo = new MailVO();
        vo.setSubject("ASA教师帐户邮箱激活");
        vo.setFromUser(Constants.EMAIL_SENDER);
        vo.setToUser(jsonObject.getString("email"));
        String checkCode = StringTools.getStringRandom(26);
        JSONObject jsonObjectActive = new JSONObject();
        jsonObjectActive.put("email",jsonObject.getString("email"));
        jsonObjectActive.put("activeCode",checkCode);
        userDao.addActiveCode(jsonObjectActive);

        //String url = "<a href='http://localhost:8080/register/activeUser?code="+checkCode+"'>激活链接</a>";
        String url = "<a target='_blank' href='"+emailConfig.getUrlhost()+"/active?code="+checkCode+"'>激活链接</a>";

        StringBuffer sb = new StringBuffer();
        sb.append("<html><head></head><body><h1>亲爱的用户： 您好！</h1>")
                .append("<p style='color:gray'>您正在使用此安全邮箱注册，激活请点击链接</p>")
                .append("<p style=''>"+url+"</p>");
        vo.setContent(sb.toString());
        EmailUtil e = new EmailUtil();
        e.sendMailHtml(vo,emailConfig);

        return CommonUtil.successJson();
    }

}
