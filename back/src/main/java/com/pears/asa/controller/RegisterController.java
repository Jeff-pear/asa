package com.pears.asa.controller;

import com.alibaba.fastjson.JSONObject;
import com.pears.asa.service.LoginService;
import com.pears.asa.service.UserService;
import com.pears.asa.util.CommonUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author: pears
 * @description: 注册相关Controller
 * @date: 2018/11/21 14:53
 */
@RestController
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    private LoginService loginService;
    @Autowired
    private UserService userService;

    /**
     * 注册
     *
     * @param requestJson
     * @return
     */
    @PostMapping("/create")
    public JSONObject createUser(@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequired(requestJson, "username,password");
        return loginService.authLogin(requestJson);
    }

    /**
     * 角色列表
     *
     * @return
     */

    @GetMapping("/listRole")
    public JSONObject listRole() {
        return userService.getRolesWithRegister();
    }

}
