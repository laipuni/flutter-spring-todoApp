package project.app.flutter_spring_todoapp.quartz.config;

import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import project.app.flutter_spring_todoapp.quartz.job.TodoReminderJob;

@Configuration
public class QuartzConfig {

    @Bean
    public Job todoReminderJob(){
        return new TodoReminderJob();
    }

    @Bean
    public JobDetail todoReminderJobDetail() {
        return JobBuilder.newJob(TodoReminderJob.class)
                .withIdentity("todoReminderJob")// Job 이름 설정
                .storeDurably()
                .build();
    }

    // 트리거 등록 (매분 실행)
    @Bean
    public Trigger todoReminderTrigger(JobDetail jobDetail) {
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity("todoReminderTrigger") // 트리거 이름
                .withSchedule(
                        SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInMinutes(1) // 1분마다 실행
                        .repeatForever()
                )
                .build();
    }

}
