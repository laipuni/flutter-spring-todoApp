package project.app.flutter_spring_todoapp.quartz.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
@Slf4j
public class TodoReminderJob implements Job {
    @Override
    public void execute(final JobExecutionContext context) throws JobExecutionException {
        log.info("[Quartz Scheduler] time = {}", LocalDateTime.now());
    }
}
