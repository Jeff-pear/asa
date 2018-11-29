package com.pears.asa.controller;

import com.alibaba.fastjson.JSONObject;
import com.pears.asa.service.SysService;
import com.pears.asa.service.UserService;
import com.pears.asa.util.CommonUtil;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: pears
 * @description: system权限相关controller
 * @date: 2018/11/23 10:19
 */
@RestController
@RequestMapping("/sys")
public class SysController {
    @Autowired
    private SysService sysService;

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
        CommonUtil.hasAllRequired(requestJson, "datePeriod");
        sysService.deletePeriods();
        sysService.addPeriod(requestJson);
        return CommonUtil.successJson();
    }

}
