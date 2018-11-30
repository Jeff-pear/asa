package com.pears.asa.config.quartz;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description
 * @Author sgl
 * @Date 2018-06-26 16:52
 */
public class CourseTask extends QuartzJobBean{
    private static Logger logger = LoggerFactory.getLogger(CourseTask.class);
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        logger.info("Execute cron task----" + sdf.format(new Date()));
    }
}