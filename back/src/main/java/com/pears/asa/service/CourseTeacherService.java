package com.pears.asa.service;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * @author: pears
 * @date: 2017/10/24 16:06
 */
public interface CourseTeacherService {
    /**
     * 新增文章
     *
     * @param jsonObject
     * @return
     */
    JSONObject addCourse(JSONObject jsonObject);

    /**
     * 文章列表
     *
     * @param jsonObject
     * @return
     */
    JSONObject listCourse(JSONObject jsonObject);
    /**
     * 文章列表
     *
     * @param jsonObject
     * @return
     */
    JSONObject listMyCourse(JSONObject jsonObject);

    /**
     * 更新文章
     *
     * @param jsonObject
     * @return
     */
    JSONObject updateCourse(JSONObject jsonObject);
    /**
     * 更新4sys
     *
     * @param jsonObject
     * @return
     */
    JSONObject updateCourse4Sys(JSONObject jsonObject);

    /**
     * 更新文章
     *
     * @param jsonObject
     * @return
     */
    JSONObject updateFinalTuition(JSONObject jsonObject);
    /**
     * 更新文章
     *
     * @param jsonObject
     * @return
     */
    JSONObject deleteCourse(JSONObject jsonObject);
    /**
     * 更新文章
     *
     * @param jsonObject
     * @return
     */
    JSONObject disabledCourse(JSONObject jsonObject);

    /**
     * 列表
     *
     * @param jsonObject
     * @return
     */
    JSONObject listAllTeacher(JSONObject jsonObject);

    /**
     * 列表
     *
     * @param jsonObject
     * @return
     */
    JSONObject listCourseResult4Finance(JSONObject jsonObject);

}
