package com.pears.asa.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.pears.asa.dao.CourseTeacherDao;
import com.pears.asa.service.CourseTeacherService;
import com.pears.asa.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    /**
     * 新增文章
     *
     * @param jsonObject
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JSONObject addCourse(JSONObject jsonObject) {
        courseTeacherDao.addCourse(jsonObject);
        return CommonUtil.successJson();
    }

    /**
     * 文章列表
     *
     * @param jsonObject
     * @return
     */
    @Override
    public JSONObject listCourse(JSONObject jsonObject) {
        CommonUtil.fillPageParam(jsonObject);
        int count = courseTeacherDao.countCourse(jsonObject);
        List<JSONObject> list = courseTeacherDao.listCourse(jsonObject);
        return CommonUtil.successPage(jsonObject, list, count);
    }

    /**
     * 更新文章
     *
     * @param jsonObject
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JSONObject updateCourse(JSONObject jsonObject) {
        courseTeacherDao.updateCourse(jsonObject);
        return CommonUtil.successJson();
    }
}
