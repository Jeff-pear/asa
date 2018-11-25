package com.pears.asa.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pears.asa.dao.SysDao;
import com.pears.asa.dao.UserDao;
import com.pears.asa.service.SysService;
import com.pears.asa.service.UserService;
import com.pears.asa.util.CommonUtil;
import com.pears.asa.util.constants.ErrorEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author: pears
 * @description: 用户/角色/权限
 * @date: 2017/11/2 10:18
 */
@Service
public class SysServiceImpl implements SysService {
    @Autowired
    private SysDao sysDao;

    @Override
    public List<JSONObject> listSysUserActive(JSONObject jsonObject) {
        return sysDao.listSysUserActive(jsonObject);
    }

    /**
     * 选课周期列表
     *
     * @param jsonObject
     * @return
     */
    @Override
    public JSONObject listPeriod(JSONObject jsonObject) {
        CommonUtil.fillPageParam(jsonObject);
        List<JSONObject> list = sysDao.listPeriod(jsonObject);
        return CommonUtil.successPage(list);
    }

    @Override
    public JSONObject addPeriod(JSONObject jsonObject) {
        JSONArray arr = jsonObject.getJSONArray("datePeriod");
        jsonObject.put("startDate",arr.get(0));
        jsonObject.put("endDate",arr.get(1));
        return CommonUtil.successJson(sysDao.addPeriod(jsonObject));
    }

    @Override
    public void deletePeriods() {
        sysDao.deletePeriods();
    }

}
