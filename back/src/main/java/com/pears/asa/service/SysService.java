package com.pears.asa.service;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * @author: pears
 * @description: Sys
 * @date: 2018/11/22 10:18
 */
public interface SysService {
    /**
     * 查询激活邮箱码列表
     *
     * @param jsonObject
     * @return
     */
    List<JSONObject> listSysUserActive(JSONObject jsonObject);
    /**
     * Period列表
     *
     * @param jsonObject
     * @return
     */
    JSONObject listPeriod(JSONObject jsonObject);

    /**
     * 设置Period列表
     *
     * @param jsonObject
     * @return
     */
    JSONObject addPeriod(JSONObject jsonObject);

    /**
     * 删除Period
     *
     */
    void deletePeriods();

    /**
     * 设置密码
     *
     * @param jsonObject
     * @return
     */
    JSONObject setPassword(JSONObject jsonObject);

    /**
     * 添加附件
     *
     * @param jsonObject
     * @return
     */
    JSONObject addAttachment(JSONObject jsonObject);

    /**
     * 查询attachment列表
     *
     * @param jsonObject
     * @return
     */
    List<JSONObject> listAttachment(JSONObject jsonObject);

    /**
     * 更新attachment
     *
     */
    JSONObject updateAttachmentBusinessById(JSONObject jsonObject);

    /**
     * 删除atta
     *
     */
    void deleteAttachmentById(JSONObject jsonObject);

}
