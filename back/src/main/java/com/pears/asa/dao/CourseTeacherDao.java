package com.pears.asa.dao;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * @author: pears
 * @description: 课程Dao层
 * @date: 2017/10/24 16:06
 */
public interface CourseTeacherDao {
    /**
     * 新增文章
     *
     * @param jsonObject
     * @return
     */
    int addCourse(JSONObject jsonObject);

    /**
     * 统计文章总数
     *
     * @param jsonObject
     * @return
     */
    int countCourse(JSONObject jsonObject);

    /**
     * 文章列表
     *
     * @param jsonObject
     * @return
     */
    List<JSONObject> listCourse(JSONObject jsonObject);

    /**
     * 更新文章
     *
     * @param jsonObject
     * @return
     */
    int updateCourse(JSONObject jsonObject);
}
