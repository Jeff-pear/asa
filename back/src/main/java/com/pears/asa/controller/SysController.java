package com.pears.asa.controller;

import com.alibaba.fastjson.JSONObject;
import com.pears.asa.config.quartz.CourseTask;
import com.pears.asa.config.quartz.NoticeTask;
import com.pears.asa.service.SysService;
import com.pears.asa.util.CommonUtil;
import com.pears.asa.util.constants.Constants;
import com.pears.asa.util.constants.ErrorEnum;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static sun.rmi.transport.TransportConstants.Magic;

/**
 * @author: pears
 * @description: system权限相关controller
 * @date: 2018/11/23 10:19
 */
@RestController
@RequestMapping("/sys")
public class SysController {
    private static Logger logger = LoggerFactory.getLogger(SysController.class);
    @Autowired
    private SysService sysService;
    @Value("${prop.upload-folder}")
    private String upLoad_Folder;

    @Autowired
    private CourseTask courseTask;

    @Autowired
    private NoticeTask noticeTask;

    /**
     * 通过userid查询用户
     *
     * @param request
     * @return
     */
    @GetMapping("/getUserById")
    public JSONObject getUserById(HttpServletRequest request) {
        List<JSONObject> result = sysService.getUserByUserId(CommonUtil.request2Json(request));
        if(result.size()>0){
            return CommonUtil.successPage(result);
        }
        return CommonUtil.errorJson(ErrorEnum.E_10009);
    }

    /**
     * 选课周期
     *
     * @param request
     * @return
     */
    @GetMapping("/listPeriod")
    public JSONObject listPeriod(HttpServletRequest request) {
        return sysService.listPeriod(CommonUtil.request2Json(request));
    }

    /**
     * attachment
     *
     * @param request
     * @return
     */
    @GetMapping("/listAttach")
    public JSONObject listAttach(HttpServletRequest request) {
        List<JSONObject> list = sysService.listAttachment(CommonUtil.request2Json(request));
        return CommonUtil.successPage(list);
    }

    /**
     * 设置选课周期
     *
     * @param requestJson
     * @return
     */
    @RequiresPermissions("sys:period")
    @PostMapping("/setPeriod")
    public JSONObject setPeriod(@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequired(requestJson, "type,teacherPeriod,studentPeriod,feePeriod,financePeriod,noticeStartDate");
        sysService.deletePeriods();
        sysService.addPeriod(requestJson);
        return CommonUtil.successJson();
    }

    /**
     * 设置密码
     *
     * @param requestJson
     * @return
     */
    @PostMapping("/setPassword")
    public JSONObject setPassword(@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequired(requestJson, "password,newPassword");
        sysService.setPassword(requestJson);
        return CommonUtil.successJson();
    }

    /**
     * 设置密码
     *
     * @param requestJson
     * @return
     */
    @PostMapping("/setGrade")
    public JSONObject setGrade(@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequired(requestJson, "grade");
        sysService.setGrade(requestJson);
        return CommonUtil.successJson();
    }

    @RequestMapping(path = "/download/{id}", method = RequestMethod.GET)
    public JSONObject downLoad(@PathVariable(name = "id") int id,
                           HttpServletResponse response){
        JSONObject j = new JSONObject();
        j.put("id",id);
        List<JSONObject> list = sysService.listAttachment(j);
        if(list.size()>0){
            String url = list.get(0).getString("location");
            String fileName = list.get(0).getString("origin_fileName");

            Path path = Paths.get(upLoad_Folder+"/"+url);
            File file = new File(path.toString());
            if(file.exists()){ //判断文件父目录是否存在

                byte[] buffer = new byte[1024];
                FileInputStream fis = null; //文件输入流
                BufferedInputStream bis = null;

                OutputStream os = null; //输出流
                try {
                    response.setContentType("application/force-download");
                    response.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(fileName, "UTF-8"));
                    os = response.getOutputStream();
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    int i = bis.read(buffer);
                    while(i != -1){
                        os.write(buffer);
                        i = bis.read(buffer);
                    }
                    return null;
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
                logger.info("----------file download" + fileName);
                try {
                    bis.close();
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        ErrorEnum err = ErrorEnum.E_10006;
        err.setErrorMsg(CommonUtil.getI18NMessage("validation.noFile",null));
        return CommonUtil.errorJson(err);
    }

    @RequestMapping(path = "/preview/{id}", method = RequestMethod.GET)
    public JSONObject preview(@PathVariable(name = "id") int id,
                               HttpServletResponse response) throws IOException {
        JSONObject j = new JSONObject();
        j.put("id",id);
        List<JSONObject> list = sysService.listAttachment(j);
        if(list.size()>0){
            String url = list.get(0).getString("location");
            String fileName = list.get(0).getString("origin_fileName");

            Path path = Paths.get(upLoad_Folder+"/"+url);
            File file = new File(path.toString());
            long fileSize = file.length();
            if(file.exists()){ //判断文件父目录是否存在
                ServletOutputStream sos = null;
                        BufferedImage img = ImageIO.read(file);
                        InputStream inputStream = null;
                try {
                    String fileType = getContentType(fileName);
                    if(fileName.toLowerCase().contains("png") || fileName.toLowerCase().contains("jpg")){
                        response.setContentType("image/png");
                        sos = response.getOutputStream();
                        ImageIO.write(img, "jpeg", sos);
                    }else {
                        response.setContentType(fileType);
                        response.setHeader("Content-Disposition", "filename=\"" + fileName + "" + "\"");
                        inputStream = new FileInputStream(file);
                        byte[] buffer = new byte[(int) fileSize];

                        int offset = 0;
                        int numRead = 0;
                        while (offset < buffer.length&& (numRead = inputStream.read(buffer, offset, buffer.length - offset)) >= 0) {
                                offset += numRead;
                                 }
                        if (offset != buffer.length) {
                            throw new IOException("Could not completely read file "+ file.getName());
                        }
                        response.getOutputStream().write(buffer);
                        response.getOutputStream().flush();
                        response.flushBuffer();
                    }

                    return null;
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
                logger.info("----------file preview : {}" , fileName);
                try {
                    if(sos!=null)
                    sos.close();
                    response.getOutputStream().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        ErrorEnum err = ErrorEnum.E_10006;
        err.setErrorMsg(CommonUtil.getI18NMessage("validation.noFile",null));
        return CommonUtil.errorJson(err);
    }
    public static String getContentType(String filePath){
        if(filePath.toLowerCase().contains("png")){
            return "application/x-png";
        }
        if(filePath.toLowerCase().contains("jpg") || filePath.toLowerCase().contains("jpeg")){
            return "image/jpeg";
        }
        if(filePath.toLowerCase().contains("pdf")){
            return "application/pdf";
        }
        if(filePath.toLowerCase().contains("doc")){
            return "application/msword";
        }
        if(filePath.toLowerCase().contains("docx")){
            return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
        }
return null;
    }


    /**
     * 新增课程时，文件上传
     *
     * @param file
     * @return
     */
    @PostMapping("/uploadFile")
    public JSONObject uploadFile(MultipartFile file) {
        if (Objects.isNull(file) || file.isEmpty()) {
            logger.error(CommonUtil.getI18NMessage("validation.fileIsNull",null));
        }
        long len = file.getSize()/1024/1024;
        if(len>5){
            return CommonUtil.errorJson(ErrorEnum.E_1000A);
        }
        try {
            byte[] bytes = file.getBytes();

            UUID fileUrl = UUID.randomUUID();
            Path path = Paths.get(upLoad_Folder + fileUrl);
            //如果没有files文件夹，则创建
            if (!Files.isWritable(path)) {
                Files.createDirectories(Paths.get(upLoad_Folder));
            }
            //文件写入指定路径
            Files.write(path, bytes);
            logger.debug("文件写入成功...");
            JSONObject result = new JSONObject();
            result.put("originalFileName",file.getOriginalFilename());
            result.put("returnCode", Constants.SUCCESS_CODE);
            result.put("location",fileUrl.toString());
            Session session = SecurityUtils.getSubject().getSession();
            JSONObject userInfo = (JSONObject) session.getAttribute(Constants.SESSION_USER_INFO);
            int userId = userInfo.getInteger("userId");
            //result.put("type","course-teacher");
            result.put("userId",userId);
            result.put("originFileName",file.getOriginalFilename());
            result = sysService.addAttachment(result);
            result.put("attachId",result.getInteger("id"));
            return CommonUtil.successJson(result);
        } catch (IOException e) {
            e.printStackTrace();
            ErrorEnum err = ErrorEnum.E_10006;
            err.setErrorMsg(e.getMessage());
            return CommonUtil.errorJson(err);
        }

    }
    /**
     * 更新附件
     *
     * @param requestJson
     * @return
     */
    @PostMapping("/updateAttachment")
    public JSONObject updateAttachment(@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequired(requestJson, "id,businessId,businessType");
        sysService.updateAttachmentBusinessById(requestJson);
        return CommonUtil.successJson();
    }

    /**
     * 删除附件
     *
     * @param requestJson
     * @return
     */
    @PostMapping("/deleteAttachment")
    public JSONObject deleteAttachment(@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequired(requestJson, "id,businessId");
        List<JSONObject> list = sysService.listAttachment(requestJson);
        if(list.size()>0){
            sysService.deleteAttachmentById(requestJson);
            Path path = Paths.get(upLoad_Folder+"/"+list.get(0).getString("location"));
            File file = new File(path.toString());
            if(file.exists()&&file.isFile())
                file.delete();
            return CommonUtil.successJson();
        }else{
            ErrorEnum err = ErrorEnum.E_10006;
            err.setErrorMsg(CommonUtil.getI18NMessage("validation.failDeleteFile",null));
            return CommonUtil.errorJson(err);
        }

    }

    /**
     * 删除附件
     *
     * @param requestJson
     * @return
     */
    @PostMapping("/deleteAttachment4Admin")
    public JSONObject deleteAttachment4Admin(@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequired(requestJson, "id");
        sysService.deleteAttachmentById(requestJson);
        return CommonUtil.successJson();

    }

    /**
     * 执行CourseTaskJob
     *
     * @param request
     * @return
     */
    @GetMapping("/execCourseTaskJob")
    public JSONObject execCourseTaskJob(HttpServletRequest request) {
        courseTask.execJob();
        return CommonUtil.successJson();
    }

    /**
     * 执行NoticeTaskJob
     *
     * @param request
     * @return
     */
    @GetMapping("/execNoticeTaskJob")
    public JSONObject execNoticeTaskJob(HttpServletRequest request) {
        noticeTask.execJob();
        return CommonUtil.successJson();
    }


}
