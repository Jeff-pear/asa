package com.pears.asa.controller;

import com.alibaba.fastjson.JSONObject;
import com.pears.asa.service.CourseTeacherService;
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
    private CourseTeacherService courseTeacherService;

    /**
     * 查询文章列表
     *
     * @param request
     * @return
     */
    @RequiresPermissions("course-student:list")
    @GetMapping("/listCourse")
    public JSONObject listCourse(HttpServletRequest request) {
        return courseTeacherService.listCourse(CommonUtil.request2Json(request));
    }

    /**
     * 新增文章
     *
     * @param requestJson
     * @return
     */
    @RequiresPermissions("course-student:add")
    @PostMapping("/addCourse")
    public JSONObject addCourse(@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequired(requestJson, "content");
        return courseTeacherService.addCourse(requestJson);
    }

    /**
     * 修改文章
     *
     * @param requestJson
     * @return
     */
    @RequiresPermissions("course-student:update")
    @PostMapping("/updateCourse")
    public JSONObject updateCourse(@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequired(requestJson, "id,content");
        return courseTeacherService.updateCourse(requestJson);
    }
}
