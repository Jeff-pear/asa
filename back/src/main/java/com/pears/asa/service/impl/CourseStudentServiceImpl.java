package com.pears.asa.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pears.asa.dao.CourseStudentDao;
import com.pears.asa.dao.SysDao;
import com.pears.asa.service.CourseStudentService;
import com.pears.asa.util.CommonUtil;
import com.pears.asa.util.constants.Constants;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author: pears
 * @description:
 * @date: 2017/10/24 16:07
 */
@Service
public class CourseStudentServiceImpl implements CourseStudentService {

    @Autowired
    private CourseStudentDao courseStudentDao;
    @Autowired
    private SysDao sysDao;

    /**
     * 新增课程
     *
     * @param jsonObject
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JSONObject selectCourse(JSONObject jsonObject) {
        Session session = SecurityUtils.getSubject().getSession();
        JSONObject userInfo = (JSONObject) session.getAttribute(Constants.SESSION_USER_INFO);
        jsonObject.put("updateUser",userInfo.getInteger("userId"));
        jsonObject.put("createUser",userInfo.getInteger("userId"));

        jsonObject.put("selectUserId",userInfo.getInteger("userId"));

        courseStudentDao.selectCourse(jsonObject);
        return CommonUtil.successJson();
    }

    /**
     * 前端arr类型转java对象
     * @param key
     * @return
     */
    private void StringToJsonArrayObj(JSONObject jsonObject, String key) {
        if (jsonObject.get(key) != null) {
            String jsonString = "";
            if(jsonObject.get(key) instanceof LinkedHashMap ){
                jsonString = JSON.toJSONString((LinkedHashMap<String, String>) jsonObject.get(key));
            }
            if(jsonObject.get(key) instanceof List ){
                jsonString = JSON.toJSONString((ArrayList) jsonObject.get(key));
            }
            jsonObject.put(key, jsonString);
        }
    }

    /**
     * 课程列表
     *
     * @param jsonObject
     * @return
     */
    @Override
    public JSONObject listCourse(JSONObject jsonObject) {
        return getList(jsonObject);
    }


    private JSONObject getList(JSONObject jsonObject) {
        CommonUtil.fillPageParam(jsonObject);
        Session session = SecurityUtils.getSubject().getSession();
        JSONObject userInfo = (JSONObject) session.getAttribute(Constants.SESSION_USER_INFO);
        jsonObject.put("selectUserId",userInfo.getInteger("userId"));
        int count = courseStudentDao.countCourse(jsonObject);
        List<JSONObject> list = courseStudentDao.listCourse(jsonObject);
        list.stream().forEach(p->{
            JSONArray gradeObj = p.getJSONArray("grade");
            if(null!=gradeObj){
                p.put("grade",gradeObj);
            }
        });
        return CommonUtil.successPage(jsonObject, list, count);
    }

    /**
     * 更新课程
     *
     * @param jsonObject
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JSONObject updateCourse(JSONObject jsonObject) {
        StringToJsonArrayObj(jsonObject, "courseDate");
        StringToJsonArrayObj(jsonObject, "grade");
        Session session = SecurityUtils.getSubject().getSession();
        JSONObject userInfo = (JSONObject) session.getAttribute(Constants.SESSION_USER_INFO);
        jsonObject.put("updateUser",userInfo.getInteger("userId"));
        courseStudentDao.updateCourse(jsonObject);
        return CommonUtil.successJson();
    }

    @Override
    public JSONObject listStudentDetail4Teacher(JSONObject jsonObject) {
        CommonUtil.fillPageParam(jsonObject);
        int count = courseStudentDao.countStudentDetail4Teacher(jsonObject);
        List<JSONObject> list = courseStudentDao.listStudentDetail4Teacher(jsonObject);
        return CommonUtil.successPage(jsonObject, list, count);
    }

}
