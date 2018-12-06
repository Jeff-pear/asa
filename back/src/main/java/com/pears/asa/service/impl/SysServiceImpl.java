package com.pears.asa.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pears.asa.dao.SysDao;
import com.pears.asa.dao.UserDao;
import com.pears.asa.service.SysService;
import com.pears.asa.util.CommonUtil;
import com.pears.asa.util.constants.Constants;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: pears
 * @description: 用户/角色/权限
 * @date: 2017/11/2 10:18
 */
@Service
public class SysServiceImpl implements SysService {
    @Autowired
    private SysDao sysDao;
    @Autowired
    private UserDao userDao;

    @Override
    public List<JSONObject> listSysUserActive(JSONObject jsonObject) {
        return sysDao.listSysUserActive(jsonObject);
    }

    /**
     * 选课周期列表
     *
     * @param jsonObject
     * @return
     */
    @Override
    public JSONObject listPeriod(JSONObject jsonObject) {
        CommonUtil.fillPageParam(jsonObject);
        List<JSONObject> list = sysDao.listPeriod(jsonObject);
        return CommonUtil.successPage(list);
    }

    @Override
    public JSONObject addPeriod(JSONObject jsonObject) {
        JSONArray arr = jsonObject.getJSONArray("teacherPeriod");
        jsonObject.put("teacherPeriodStartDate",arr.get(0));
        jsonObject.put("teacherPeriodEndDate",arr.get(1));

        JSONArray arr2 = jsonObject.getJSONArray("studentPeriod");
        jsonObject.put("studentPeriodStartDate",arr2.get(0));
        jsonObject.put("studentPeriodEndDate",arr2.get(1));

        JSONArray arr3 = jsonObject.getJSONArray("feePeriod");
        jsonObject.put("feePeriodStartDate",arr3.get(0));
        jsonObject.put("feePeriodEndDate",arr3.get(1));

        JSONArray arr4 = jsonObject.getJSONArray("financePeriod");
        jsonObject.put("financePeriodStartDate",arr4.get(0));
        jsonObject.put("financePeriodEndDate",arr4.get(1));

        jsonObject.put("delType",jsonObject.getString("type"));


        return CommonUtil.successJson(sysDao.addPeriod(jsonObject));
    }

    @Override
    public void deletePeriods() {
        sysDao.deletePeriods();
    }

    @Override
    public JSONObject setPassword(JSONObject jsonObject) {
        Session session = SecurityUtils.getSubject().getSession();
        JSONObject userInfo = (JSONObject) session.getAttribute(Constants.SESSION_USER_INFO);
        int userId = userInfo.getInteger("userId");
        jsonObject.put("curUserId",userId);
        jsonObject.put("curPassword",jsonObject.getString("password"));
        List<JSONObject> list = userDao.listUser(jsonObject);
        if(list.size()>0){
            jsonObject.put("userId",userId);
            jsonObject.put("password",jsonObject.getString("newPassword"));
            userDao.updateUser(jsonObject);
        }

        return null;
    }

    @Override
    public JSONObject addAttachment(JSONObject jsonObject) {
        sysDao.addAttachment(jsonObject);
        return jsonObject;
    }

    @Override
    public List<JSONObject> listAttachment(JSONObject jsonObject) {
        return sysDao.listAttachment(jsonObject);
    }

    @Override
    public JSONObject updateAttachmentBusinessById(JSONObject jsonObject) {
        sysDao.updateAttachmentBusinessById(jsonObject);
        return CommonUtil.successJson();
    }

}
