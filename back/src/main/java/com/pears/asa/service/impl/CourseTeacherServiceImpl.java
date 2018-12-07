package com.pears.asa.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pears.asa.dao.CourseStudentDao;
import com.pears.asa.dao.CourseTeacherDao;
import com.pears.asa.dao.SysDao;
import com.pears.asa.service.CourseStudentService;
import com.pears.asa.service.CourseTeacherService;
import com.pears.asa.util.CommonUtil;
import com.pears.asa.util.constants.Constants;
import com.pears.asa.util.constants.ErrorEnum;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
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
    @Autowired
    private CourseStudentDao courseStudentDao;


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
        if (checkCourseDate(jsonObject, userInfo)) return CommonUtil.errorJson(ErrorEnum.E_10004);
        jsonObject.put("updateUser",userInfo.getInteger("userId"));
        jsonObject.put("createUser",userInfo.getInteger("userId"));
        StringToJsonArrayObj(jsonObject, "grade");
        List<JSONObject> listPeriod = sysDao.listPeriod(new JSONObject());
        if(listPeriod.size()>0){
            jsonObject.put("startDate",listPeriod.get(0).getString("startDate"));
            jsonObject.put("endDate",listPeriod.get(0).getString("endDate"));
        }
        courseTeacherDao.addCourse(jsonObject);
        return CommonUtil.successJson(jsonObject);
    }

    private boolean checkCourseDate(JSONObject jsonObject, JSONObject userInfo) {
        List findInSetParams = (List<String>) jsonObject.get("courseDateArr");
        if(findInSetParams.size()>0){
            JSONObject jo = new JSONObject();
            jo.put("courseDateArr", findInSetParams);
            jo.put("author", userInfo.getInteger("userId"));
            jo.put("idNotEqual", jsonObject.getInteger("idNotEqual"));
            int count = courseTeacherDao.countCourse(jo);
            if(count>0){
                return true;
            }
        }
        return false;
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
        if(jsonObject.containsKey("studentCanPick") && jsonObject.getBoolean("studentCanPick")){
            Session session = SecurityUtils.getSubject().getSession();
            JSONObject userInfo = (JSONObject) session.getAttribute(Constants.SESSION_USER_INFO);
            jsonObject.put("studentCanPick",userInfo.getInteger("userId"));
        }
        return getList(jsonObject);
    }

    /**
     * 我的课程列表
     *
     * @param jsonObject
     * @return
     */
    @Override
    public JSONObject listMyCourse(JSONObject jsonObject) {
        Session session = SecurityUtils.getSubject().getSession();
        JSONObject userInfo = (JSONObject) session.getAttribute(Constants.SESSION_USER_INFO);
        jsonObject.put("author",userInfo.getInteger("userId"));
        JSONObject result = getList(jsonObject);
        List<JSONObject>  list = (List<JSONObject> )result.getJSONObject("returnData").get("list");
        list.stream().forEach(i->{
            JSONObject j = new JSONObject();
            j.put("courseId",i.get("id"));
            int num = courseStudentDao.countStudentDetail4Teacher(j);
            i.put("pickStudentNum",num);
        });
        return result;
    }

    private JSONObject getList(JSONObject jsonObject) {
        CommonUtil.fillPageParam(jsonObject);
        int count = courseTeacherDao.countCourse(jsonObject);
        List<JSONObject> list = courseTeacherDao.listCourse(jsonObject);
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
        Session session = SecurityUtils.getSubject().getSession();
        JSONObject userInfo = (JSONObject) session.getAttribute(Constants.SESSION_USER_INFO);
        jsonObject.put("idNotEqual",jsonObject.getInteger("id"));
        if (checkCourseDate(jsonObject, userInfo)) return CommonUtil.errorJson(ErrorEnum.E_10004);
        exeUpdateCourse(jsonObject);
        return CommonUtil.successJson();
    }

    /**
     * 更新课程
     *
     * @param jsonObject
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JSONObject deleteCourse(JSONObject jsonObject) {

        JSONObject jo = new JSONObject();
        jo.put("courseId",jsonObject.getInteger("id"));
        int canDelete = courseStudentDao.countCourse(jo);
        if(canDelete>0){
            return CommonUtil.errorJson(ErrorEnum.E_10005);
        }
        exeUpdateCourse(jsonObject);
        return CommonUtil.successJson();
    }

    @Override
    public JSONObject listAllTeacher(JSONObject jsonObject) {
        return CommonUtil.successJson(courseTeacherDao.listAllTeacher(jsonObject));
    }

    private void exeUpdateCourse(JSONObject jsonObject) {
        StringToJsonArrayObj(jsonObject, "grade");
        Session session = SecurityUtils.getSubject().getSession();
        JSONObject userInfo = (JSONObject) session.getAttribute(Constants.SESSION_USER_INFO);
        jsonObject.put("updateUser",userInfo.getInteger("userId"));
        courseTeacherDao.updateCourse(jsonObject);
    }

}
