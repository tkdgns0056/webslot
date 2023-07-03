package com.example.slotweb.schedule;

import com.example.slotweb.service.slot.SlotService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class MyScheduler {

    final private SlotService slotService;

    //@Scheduled(fixedRate = 5000) // 5초마다 실행
    @Scheduled(cron = "0 0/30 * * * ?") // 5초마다 실행
    public void scheduledTask() {
        // 스케줄링 작업 수행
        slotService.deleteBatchSlot();
        //System.out.println("Scheduled task executed.");
    }
}