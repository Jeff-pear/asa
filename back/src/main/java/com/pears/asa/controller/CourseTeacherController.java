package com.pears.asa.controller;

import com.alibaba.fastjson.JSONObject;
import com.pears.asa.service.CourseTeacherService;
import com.pears.asa.service.SysService;
import com.pears.asa.util.CommonUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: pears
 * @description: 课程相关Controller
 * @date: 2017/10/24 16:04
 */
@RestController
@RequestMapping("/course-teacher")
public class CourseTeacherController {
    private static Logger logger = LoggerFactory.getLogger(CourseTeacherController.class);
    @Autowired
    private CourseTeacherService courseTeacherService;
    @Value("${prop.upload-folder}")
    private String upLoad_Folder;

    @Autowired
    private SysService sysService;

    /**
     * 查询课程列表
     *
     * @param request
     * @return
     */
    @RequiresPermissions("course-teacher:list")
    @GetMapping("/listCourse")
    public JSONObject listCourse(HttpServletRequest request) {
        return courseTeacherService.listCourse(CommonUtil.request2Json(request));
    }

    /**
     * 查询我的课程列表
     *
     * @param request
     * @return
     */
    @RequiresPermissions("course-teacher:list")
    @GetMapping("/myListCourse")
    public JSONObject listMyCourse(HttpServletRequest request) {
        return courseTeacherService.listMyCourse(CommonUtil.request2Json(request));
    }

    /**
     * 查询所有教师
     *
     * @param request
     * @return
     */
    @RequiresPermissions("course-teacher:update")
    @GetMapping("/listAllTeacher")
    public JSONObject listAllTeacher(HttpServletRequest request) {
        return courseTeacherService.listAllTeacher(CommonUtil.request2Json(request));
    }

    /**
     * 查询student sign up course result
     *
     * @param request
     * @return
     */
    @RequiresPermissions("course-finance:list")
    @GetMapping("/listCourseResult4Finance")
    public JSONObject listCourseResult4Finance(HttpServletRequest request) {
        return courseTeacherService.listCourseResult4Finance(CommonUtil.request2Json(request));
    }

    /**
     * 查询notice result
     *
     * @param request
     * @return
     */
    @RequiresPermissions("course-notice:list")
    @GetMapping("/listCourseResult4Notice")
    public JSONObject listCourseResult4Notice(HttpServletRequest request) {
        JSONObject j = CommonUtil.request2Json(request);
        j.put("statusSuccess","true");
        return courseTeacherService.listCourseResult4Finance(j);
    }

   /**
     * 新增课程
     *
     * @param requestJson
     * @return
     */
    @RequiresPermissions("course-teacher:add")
    @PostMapping("/addCourse")
    public JSONObject addCourse(@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequired(requestJson, "content,teacherName,capacity,courseDate");
        return courseTeacherService.addCourse(requestJson);
    }


    /**
     * 修改课程
     *
     * @param requestJson
     * @return
     */
    @RequiresPermissions("course-teacher:update")
    @PostMapping("/updateCourse")
    public JSONObject updateCourse(@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequired(requestJson, "id, stepActive");
        if(requestJson.getInteger("stepActive")==1){
            CommonUtil.hasAllRequired(requestJson, "content,teacherName,capacity,courseDate");
        }else{
            CommonUtil.hasAllRequired(requestJson, "grade,teacherType,tuitionType");
        }

        return courseTeacherService.updateCourse(requestJson);
    }
    @RequiresPermissions("course-teacher:update")
    @PostMapping("/updateFinalTuition")
    public JSONObject updateFinalTuition(@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequired(requestJson, "id, finalTuition");
        return courseTeacherService.updateFinalTuition(requestJson);
    }

    @RequiresPermissions("course-teacher:update")
    @PostMapping("/updateCourseArea")
    public JSONObject updateCourseArea(@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequired(requestJson, "id, courseArea");
        return courseTeacherService.updateFinalTuition(requestJson);
    }


    /**
     * 删除课程
     *
     * @param requestJson
     * @return
     */
    @RequiresPermissions("course-teacher:delete")
    @PostMapping("/deleteCourse")
    public JSONObject deleteCourse(@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequired(requestJson, "id");

        JSONObject json = new JSONObject();
        json.put("id",requestJson.getInteger("id"));
        json.put("deleteStatus","2");
        return courseTeacherService.deleteCourse(json);
    }
}
