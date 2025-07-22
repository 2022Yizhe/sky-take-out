package com.sky.task;


import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


@Component
@Slf4j
public class MyTask {

    /**
     * 每 5 秒执行一次 log
     */
//    @Scheduled(cron = "0/5 * * * * ?")
    public void executeExample() {
        log.info("[Task] 定时任务开始执行 -- " + LocalDateTime.now());
    }
}
