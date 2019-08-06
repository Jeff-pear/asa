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

//
//        @Bean
//        public JobDetail noticeQuartz() {
//            return JobBuilder.newJob(CourseTask.class).withIdentity("sysTask").storeDurably().build();
//        }
//
//        @Bean
//        public Trigger quartzNoticeTrigger() {
//            //cron方式
//            return TriggerBuilder.newTrigger().forJob(noticeQuartz())
//                    .withIdentity("sysTask")
//                    .withSchedule(CronScheduleBuilder.cronSchedule("0 00 01 * * ?"))
//                    //.withSchedule(CronScheduleBuilder.cronSchedule("0 0/1 * * * ? *"))
//                    .build();
//        }

        @Bean
        public JobDetail dailyQuartz() {
            return JobBuilder.newJob(NoticeTask.class).withIdentity("dailyTask").storeDurably().build();
        }

        @Bean
        public Trigger quartzDailyTrigger() {
            //cron方式
            return TriggerBuilder.newTrigger().forJob(dailyQuartz())
                    .withIdentity("dailyTask")
                    .withSchedule(CronScheduleBuilder.cronSchedule("0 00 23 * * ?"))
                    //.withSchedule(CronScheduleBuilder.cronSchedule("0 0/1 * * * ? *"))
                    .build();
        }


    }

