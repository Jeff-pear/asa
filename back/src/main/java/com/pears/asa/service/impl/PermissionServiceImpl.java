package com.pears.asa.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.pears.asa.dao.PermissionDao;
import com.pears.asa.dao.SysDao;
import com.pears.asa.service.PermissionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springfox.documentation.spring.web.json.Json;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author: pears
 * @description:
 * @date: 2017/10/30 13:15
 */
@Service
public class PermissionServiceImpl implements PermissionService {
    private Logger logger = LoggerFactory.getLogger(PermissionServiceImpl.class);

    @Autowired
    private PermissionDao permissionDao;
    @Autowired
    private SysDao sysDao;

    /**
     * 查询某用户的 角色  菜单列表   权限列表
     *
     * @param username
     * @return
     */
    @Override
    public JSONObject getUserPermission(String username) {
        JSONObject userPermission = getUserPermissionFromDB(username);
        return userPermission;
    }

    /**
     * 从数据库查询用户权限信息
     *
     * @param username
     * @return
     */
    private JSONObject getUserPermissionFromDB(String username) {
        JSONObject userPermission = permissionDao.getUserPermission(username);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        //管理员角色ID为1
        int adminRoleId = -1;
        //如果是管理员
        String roleIdKey = "groupTag";
        JSONObject can = new JSONObject();
        if (adminRoleId == userPermission.getIntValue(roleIdKey)) {
            //查询所有菜单  所有权限
            Set<String> menuList = permissionDao.getAllMenu();
            Set<String> permissionList = permissionDao.getAllPermission();
            userPermission.put("menuList", menuList);
            userPermission.put("permissionList", permissionList);
        }else{
            List<JSONObject> list = sysDao.listPeriod(new JSONObject());
            if(list.size()>0){
                Date curDate = list.get(0).getDate("curDate");

                Date pickStart = list.get(0).getDate("pickStartDate");
                Date pickEnd = list.get(0).getDate("pickEndDate");

                Date teacherStartDate = list.get(0).getDate("teacherStartDate");
                Date teacherEndDate = list.get(0).getDate("teacherEndDate");

                Date feeStartDate = list.get(0).getDate("feeStartDate");
                Date feeEndDate = list.get(0).getDate("feeEndDate");

                Date financeStartDate = list.get(0).getDate("financeStartDate");
                Date financeEndDate = list.get(0).getDate("financeEndDate");

                Date noticeStartDate = list.get(0).getDate("noticeStartDate");

                can.put("canPick",false);
                can.put("canTeacher",false);
                can.put("canFee",false);
                can.put("canFinance",false);
                can.put("canNotice",false);

                if(curDate.getTime()>=pickStart.getTime() && curDate.getTime()<=pickEnd.getTime()){
                    can.put("canPick",true);
                }
                if(curDate.getTime()>=teacherStartDate.getTime() && curDate.getTime()<=teacherEndDate.getTime()){
                    can.put("canTeacher",true);
                }
                if(curDate.getTime()>=feeStartDate.getTime() && curDate.getTime()<=feeEndDate.getTime()){
                    can.put("canFee",true);
                }
                if(curDate.getTime()>=financeStartDate.getTime() && curDate.getTime()<=financeEndDate.getTime()){
                    can.put("canFinance",true);
                }
                if(curDate.getTime()>=noticeStartDate.getTime()){
                    can.put("canNotice",true);
                }
            }
        }
        userPermission.put("period",can);
        return userPermission;
    }
}
