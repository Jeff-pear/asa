package com.pears.asa.dao;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * @author: pears
 * @description: 学生课程Dao层
 * @date: 2018/11/29 16:06
 */
public interface CourseStudentDao {
    /**
     * 新增文章
     *
     * @param jsonObject
     * @return
     */
    int selectCourse(JSONObject jsonObject);

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

    /**
     * 学生列表
     *
     * @param jsonObject
     * @return
     */
    List<JSONObject> listStudentDetail4Teacher(JSONObject jsonObject);

    /**
     * 统计文章总数
     *
     * @param jsonObject
     * @return
     */
    int countStudentDetail4Teacher(JSONObject jsonObject);


}
