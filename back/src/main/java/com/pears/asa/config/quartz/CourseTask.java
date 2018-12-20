package com.pears.asa.config.quartz;

import com.alibaba.fastjson.JSONObject;
import com.pears.asa.config.properties.SystemConfig;
import com.pears.asa.dao.CourseStudentDao;
import com.pears.asa.dao.SysDao;
import com.pears.asa.service.CourseTeacherService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Description
 * @Author sgl
 * @Date 2018-06-26 16:52
 */
public class CourseTask extends QuartzJobBean{
    private static Logger logger = LoggerFactory.getLogger(CourseTask.class);
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
        logger.info("------------Execute cron task start !!!------------" + sdf.format(startDate));

        List<JSONObject> listPeriod = sysDao.listPeriod(new JSONObject());

        if(listPeriod.size()>0 && listPeriod.get(0).getDate("financeEndDate")!=null){
            if(startDate.getTime()>listPeriod.get(0).getDate("financeEndDate").getTime()){
                Set set = new HashSet<String>();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("statusPublish","true");
                JSONObject result = courseTeacherService.listCourseResult4Finance(jsonObject);
                List<JSONObject>  list = (List<JSONObject> )result.getJSONObject("returnData").get("list");

                list.stream().forEach(i->{
                    JSONObject j = new JSONObject();
                    j.put("courseId",i.get("courseId"));
                    j.put("financeIsPay","1");
                    int num = courseStudentDao.countStudentDetail4Teacher(j);

                    if(num>= Integer.parseInt(systemConfig.getCourseMminimumStudent())){
                        set.add(i.getString("courseId"));
                    }
                });
                logger.info("系统自动执行更新的course-teacher ids:"+set.toString());
                Iterator it = set.iterator();
                while (it.hasNext()){
                    JSONObject updateCourseJson = new JSONObject();
                    updateCourseJson.put("id",it.next());
                    updateCourseJson.put("statusSuccess","success");
                    courseTeacherService.updateCourse4Sys(updateCourseJson);
                    logger.info("更新成功:course-teacher id:"+updateCourseJson.getString("id"));
                }
            }
        }


        logger.info("------------Execute cron task end !!!------------" + sdf.format(new Date()));
    }
}
