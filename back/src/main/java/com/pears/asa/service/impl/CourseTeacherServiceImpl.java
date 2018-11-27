package com.pears.asa.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pears.asa.dao.CourseTeacherDao;
import com.pears.asa.dao.SysDao;
import com.pears.asa.service.CourseTeacherService;
import com.pears.asa.util.CommonUtil;
import com.pears.asa.util.constants.Constants;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author: pears
 * @description:
 * @date: 2017/10/24 16:07
 */
@Service
public class CourseTeacherServiceImpl implements CourseTeacherService {

    @Autowired
    private CourseTeacherDao courseTeacherDao;
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
    public JSONObject addCourse(JSONObject jsonObject) {
        Session session = SecurityUtils.getSubject().getSession();
        JSONObject userInfo = (JSONObject) session.getAttribute(Constants.SESSION_USER_INFO);
        jsonObject.put("updateUser",userInfo.getInteger("userId"));
        jsonObject.put("createUser",userInfo.getInteger("userId"));

        if(jsonObject.get("courseDate")!=null){
            String jsonString = JSON.toJSONString((LinkedHashMap<String,String>) jsonObject.get("courseDate"));
            jsonObject.put("courseDate",jsonString);
        }
        List<JSONObject> listPeriod = sysDao.listPeriod(new JSONObject());
        if(listPeriod.size()>0){
            jsonObject.put("startDate",listPeriod.get(0).getString("startDate"));
            jsonObject.put("endDate",listPeriod.get(0).getString("endDate"));
        }
        courseTeacherDao.addCourse(jsonObject);
        return CommonUtil.successJson();
    }

    /**
     * 课程列表
     *
     * @param jsonObject
     * @return
     */
    @Override
    public JSONObject listCourse(JSONObject jsonObject) {
        CommonUtil.fillPageParam(jsonObject);
        int count = courseTeacherDao.countCourse(jsonObject);
        List<JSONObject> list = courseTeacherDao.listCourse(jsonObject);
        list.stream().forEach(p->{
            JSONObject courseDateObj = p.getJSONObject("courseDate");
            if(null!=courseDateObj){
                p.put("courseDate",courseDateObj);
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
        if(jsonObject.get("courseDate")!=null){
            String jsonString = JSON.toJSONString((LinkedHashMap<String,String>) jsonObject.get("courseDate"));
            jsonObject.put("courseDate",jsonString);
        }
        Session session = SecurityUtils.getSubject().getSession();
        JSONObject userInfo = (JSONObject) session.getAttribute(Constants.SESSION_USER_INFO);
        jsonObject.put("updateUser",userInfo.getInteger("userId"));
        courseTeacherDao.updateCourse(jsonObject);
        return CommonUtil.successJson();
    }

}
