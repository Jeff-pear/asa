package com.pears.asa.config.quartz;


import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

    /**
     * @Description
     * @Author sgl
     * @Date 2018-06-26 16:45
     */
    @Configuration
    public class QuartzConfig {


        @Bean
        public JobDetail testQuartz2() {
            return JobBuilder.newJob(CourseTask.class).withIdentity("sysTask").storeDurably().build();
        }

        @Bean
        public Trigger testQuartzTrigger2() {
            //cron方式
            return TriggerBuilder.newTrigger().forJob(testQuartz2())
                    .withIdentity("sysTask")
                    .withSchedule(CronScheduleBuilder.cronSchedule("0 06 18 * * ?"))
                    .build();
        }


    }

