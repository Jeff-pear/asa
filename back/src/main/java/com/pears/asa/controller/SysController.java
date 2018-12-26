package com.pears.asa.controller;

import com.alibaba.fastjson.JSONObject;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

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
    @RequiresPermissions("sys:period")
    @PostMapping("/setPassword")
    public JSONObject setPassword(@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequired(requestJson, "password,newPassword");
        sysService.setPassword(requestJson);
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


}
