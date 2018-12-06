package com.pears.asa.controller;

import com.alibaba.fastjson.JSONObject;
import com.pears.asa.service.SysService;
import com.pears.asa.util.CommonUtil;
import com.pears.asa.util.constants.ErrorEnum;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

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
        err.setErrorMsg("文件未找到！");
        return CommonUtil.errorJson(err);
    }

    /**
     * 更新附件
     *
     * @param requestJson
     * @return
     */
    @PostMapping("/updateAttachment")
    public JSONObject updateAttachment(@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequired(requestJson, "id,businessId");
        sysService.updateAttachmentBusinessById(requestJson);
        return CommonUtil.successJson();
    }



}
