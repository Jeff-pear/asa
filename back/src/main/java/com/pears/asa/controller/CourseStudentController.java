package com.pears.asa.controller;

import com.alibaba.fastjson.JSONObject;
import com.pears.asa.service.CourseStudentService;
import com.pears.asa.util.CommonUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: pears
 * @description: 课程相关Controller
 * @date: 2018/11/26 16:04
 */
@RestController
@RequestMapping("/course-student")
public class CourseStudentController {

    @Autowired
    private CourseStudentService courseStudentService;

    /**
     * 查询列表
     *
     * @param request
     * @return
     */
    @RequiresPermissions("course-student:list")
    @GetMapping("/listCourse")
    public JSONObject listCourse(HttpServletRequest request) {
        return courseStudentService.listCourse(CommonUtil.request2Json(request));
    }

    /**
     * 新增
     *
     * @param requestJson
     * @return
     */
    @RequiresPermissions("course-student:update")
    @PostMapping("/pickCourse")
    public JSONObject pickCourse(@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequired(requestJson, "courseId");
        return courseStudentService.selectCourse(requestJson);
    }

    /**
     * 修改
     *
     * @param requestJson
     * @return
     */
    @RequiresPermissions("course-student:update")
    @PostMapping("/updateCourse")
    public JSONObject updateCourse(@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequired(requestJson, "id,courseId");
        return courseStudentService.updateCourse(requestJson);
    }

    /**
     * 修改
     *
     * @param requestJson
     * @return
     */
    @RequiresPermissions("course-student:update")
    @PostMapping("/deleteCourse")
    public JSONObject deleteCourse(@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequired(requestJson, "id");
        requestJson.put("deleteStatus","2");
        return courseStudentService.updateCourse(requestJson);
    }
}
