package com.pears.asa.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.pears.asa.config.exception.UnActiveUserException;
import com.pears.asa.dao.LoginDao;
import com.pears.asa.service.LoginService;
import com.pears.asa.service.PermissionService;
import com.pears.asa.util.CommonUtil;
import com.pears.asa.util.constants.Constants;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: pears
 * @description: 登录service实现类
 * @date: 2017/10/24 11:53
 */
@Service
public class LoginServiceImpl implements LoginService {
    private Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);

    @Autowired
    private LoginDao loginDao;
    @Autowired
    private PermissionService permissionService;

    /**
     * 登录表单提交
     *
     * @param jsonObject
     * @return
     */
    @Override
    public JSONObject authLogin(JSONObject jsonObject) {
        String username = jsonObject.getString("username");
        String password = jsonObject.getString("password");
        JSONObject returnData = new JSONObject();
        Subject currentUser = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        try {
            currentUser.login(token);
            logger.info("User :"+token.getUsername()+" Login successfully!");
            returnData.put("result", "success");
        } catch (AuthenticationException e) {
            returnData.put("result", "fail");
            if(UnActiveUserException.class.equals(e.getClass())){
                returnData.put("errorMessage", ((UnActiveUserException)e).getResultJson().getString("errorMessage"));
            }
        }
        return CommonUtil.successJson(returnData);
    }

    /**
     * 根据用户名和密码查询对应的用户
     *
     * @param username 用户名
     * @param password 密码
     * @return
     */
    @Override
    public JSONObject getUser(String username, String password) {
        return loginDao.getUser(username, password);
    }

    /**
     * 查询当前登录用户的权限等信息
     *
     * @return
     */
    @Override
    public JSONObject getInfo() {
        //从session获取用户信息
        Session session = SecurityUtils.getSubject().getSession();
        JSONObject userInfo = (JSONObject) session.getAttribute(Constants.SESSION_USER_INFO);
        String username = userInfo.getString("username");
        JSONObject returnData = new JSONObject();
        JSONObject userPermission = permissionService.getUserPermission(username);
        session.setAttribute(Constants.SESSION_USER_PERMISSION, userPermission);
        returnData.put("userPermission", userPermission);
        returnData.put("needResetPwd", userInfo.get("needResetPwd"));
        return CommonUtil.successJson(returnData);
    }

    /**
     * 退出登录
     *
     * @return
     */
    @Override
    public JSONObject logout() {
        try {
            Subject currentUser = SecurityUtils.getSubject();
            currentUser.logout();
        } catch (Exception e) {
        }
        return CommonUtil.successJson();
    }
}
