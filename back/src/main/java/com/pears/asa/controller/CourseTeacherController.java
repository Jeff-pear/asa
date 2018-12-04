package com.pears.asa.controller;

import com.alibaba.fastjson.JSONObject;
import com.pears.asa.service.CourseTeacherService;
import com.pears.asa.util.CommonUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

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
     * 新增课程时，文件上传
     *
     * @param file
     * @return
     */
    @RequiresPermissions("course-teacher:add")
    @PostMapping("/uploadFile")
    @ResponseBody
    public JSONObject uploadFile(MultipartFile file) {
        if (Objects.isNull(file) || file.isEmpty()) {
            logger.error("文件为空");
        }

        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(upLoad_Folder + file.getOriginalFilename());
            //如果没有files文件夹，则创建
            if (!Files.isWritable(path)) {
                Files.createDirectories(Paths.get(upLoad_Folder));
            }
            //文件写入指定路径
            Files.write(path, bytes);
            logger.debug("文件写入成功...");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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
        CommonUtil.hasAllRequired(requestJson, "content,capacity,courseDate");
        return courseTeacherService.addCourse(requestJson);
    }


    **
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
            CommonUtil.hasAllRequired(requestJson, "content,capacity,courseDate");
        }else{
            CommonUtil.hasAllRequired(requestJson, "grade,teacherType,tuitionType");
        }

        return courseTeacherService.updateCourse(requestJson);
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
