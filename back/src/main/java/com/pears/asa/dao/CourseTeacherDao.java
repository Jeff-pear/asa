package com.pears.asa.dao;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Param;

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

    /**
     * 更新文章
     *
     * @param jsonObject
     * @return
     */
    int updateFinalTuition(JSONObject jsonObject);
    /**
     * 文章列表
     *
     * @param jsonObject
     * @return
     */
    List<JSONObject> listAllTeacher(JSONObject jsonObject);

    /**
     * 列表
     *
     * @param jsonObject
     * @return
     */
    List<JSONObject> listCourseResult4Finance(JSONObject jsonObject);

    /**
     * 数
     *
     * @param jsonObject
     * @return
     */
    int countCourseResult4Finance(JSONObject jsonObject);
}
