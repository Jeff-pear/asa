package com.pears.asa.service;

import com.alibaba.fastjson.JSONObject;

/**
 * @author: pears
 * @date: 2018/11/26 16:06
 */
public interface CourseStudentService {
    /**
     * 新增文章
     *
     * @param jsonObject
     * @return
     */
    JSONObject selectCourse(JSONObject jsonObject);

    /**
     * 文章列表
     *
     * @param jsonObject
     * @return
     */
    JSONObject listCourse(JSONObject jsonObject);

    /**
     * 更新文章
     *
     * @param jsonObject
     * @return
     */
    JSONObject updateCourse(JSONObject jsonObject);

    /**
     * 学生列表
     *
     * @param jsonObject
     * @return
     */
    JSONObject listStudentDetail4Teacher(JSONObject jsonObject);

}
