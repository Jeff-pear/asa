package com.pears.asa.controller;

import com.alibaba.fastjson.JSONObject;
import com.pears.asa.service.RegisterService;
import com.pears.asa.service.SysService;
import com.pears.asa.service.UserService;
import com.pears.asa.util.CommonUtil;
import com.pears.asa.util.constants.Constants;
import com.pears.asa.util.constants.ErrorEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author: pears
 * @description: 注册相关Controller
 * @date: 2018/11/24 14:53
 */
@RestController
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    private RegisterService registerService;
    @Autowired
    private UserService userService;
    @Autowired
    private SysService sysService;

    /**
     * 注册
     *
     * @param requestJson
     * @return
     */
    @PostMapping("/create")
    public JSONObject createUser(@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequired(requestJson, "username,password,nickname,roleId");
        return registerService.createUser(requestJson);
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

    /**
     * 角色列表
     *
     * @return
     */

    @GetMapping("/activeUser")
    public JSONObject activeUser(HttpServletRequest request) {

        String code = request.getParameter("code");

        if(!StringUtils.isEmpty(code)){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("activeCode",code);
            List<JSONObject> list = sysService.listSysUserActive(jsonObject);
            if(!CollectionUtils.isEmpty(list)){
                JSONObject obj = list.get(0);
                obj.put("activeStatus",Constants.USER_ACTIVE);
                userService.updateUserByEmail(obj);
            }
        }
        return CommonUtil.successJson();
    }

    /**
     * 校验email
     * @param request
     * @return
     */

    @GetMapping("/checkEmail")
    public JSONObject checkEmail(HttpServletRequest request) {
        JSONObject requestJson = CommonUtil.request2Json(request);
        CommonUtil.hasAllRequired(requestJson, "email");
        if(userService.countUserByCondition(requestJson)>0){
            return CommonUtil.errorJson(ErrorEnum.E_00001);
        }else{
            return CommonUtil.successJson();
        }
    }

    /**
     * nickname
     * @param request
     * @return
     */

    @GetMapping("/checkUsername")
    public JSONObject checkNickname(HttpServletRequest request) {
        JSONObject requestJson = CommonUtil.request2Json(request);
        CommonUtil.hasAllRequired(requestJson, "username");

        if(userService.countUserByCondition(requestJson)>0){
            return CommonUtil.errorJson(ErrorEnum.E_00002);
        }else{
            return CommonUtil.successJson();
        }
    }
}
