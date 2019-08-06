package com.pears.asa.config.quartz;

import com.alibaba.fastjson.JSONObject;
import com.pears.asa.config.properties.EmailConfig;
import com.pears.asa.config.properties.SystemConfig;
import com.pears.asa.dao.CourseStudentDao;
import com.pears.asa.dao.SysDao;
import com.pears.asa.service.CourseTeacherService;
import com.pears.asa.util.CommonUtil;
import com.pears.asa.util.EmailUtil;
import com.pears.asa.util.constants.Constants;
import com.pears.asa.util.model.MailVO;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Description
 * @Author sgl
 * @Date 2018-06-26 16:52
 */
@Service
public class NoticeTask extends QuartzJobBean{
    private static Logger logger = LoggerFactory.getLogger(NoticeTask.class);
    @Autowired
    EmailConfig emailConfig;
    @Autowired
    private CourseTeacherService courseTeacherService;
    @Autowired
    private CourseStudentDao courseStudentDao;
    @Autowired
    private SysDao sysDao;
    @Autowired
    SystemConfig systemConfig;
    @Override
    public void executeInternal(JobExecutionContext context) throws JobExecutionException {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startDate = new Date();
        logger.info("------------Execute daily cron task start !!!------------" + sdf.format(startDate));

        List<JSONObject> listPeriod = sysDao.listPeriod(new JSONObject());

        if(listPeriod.size()>0 && listPeriod.get(0).getDate("financeEndDate")!=null && listPeriod.get(0).getDate("financeStartDate")!=null){
            if(startDate.getTime()>listPeriod.get(0).getDate("financeStartDate").getTime() && startDate.getTime()<listPeriod.get(0).getDate("financeEndDate").getTime()){
                execJob();
            }
        }

        logger.info("------------Execute daily cron task end !!!------------" + sdf.format(new Date()));

    }

    public void execJob() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("notContainSystem","system");
        JSONObject result = courseTeacherService.listCourseResult4Finance(jsonObject);
        List<JSONObject>  list = (List<JSONObject> )result.getJSONObject("returnData").get("list");
        logger.info("系统自动执行更新free list.size(): {}",list.size());
        list.stream().forEach(i->{
            if(i.getString("finalTuition")!=null &&
                    (i.getString("finalTuition").contains("Free") || i.getString("finalTuition").contains("free")) &&
                    (i.getString("update_by") == null || !i.getString("update_by").equalsIgnoreCase("system"))){
                JSONObject j = new JSONObject();
                j.put("id",i.get("id"));
                j.put("financeIsPay","1");
                j.put("isPay","1");
                j.put("updateBy","system");
                courseStudentDao.updateCourse(j);
                logger.info("系统自动执行更新free 变为'已交费'的 ids:"+i.get("id"));
            }
        });
    }
}
