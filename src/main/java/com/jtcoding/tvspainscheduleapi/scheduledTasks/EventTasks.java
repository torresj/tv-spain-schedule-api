package com.jtcoding.tvspainscheduleapi.scheduledTasks;

import com.jtcoding.tvspainscheduleapi.repositories.EventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class EventTasks {

    private final EventRepository eventRepository;

    @Scheduled(cron = "0 0 3 * * ?")
    public void deleteOldEvents() {
        log.info("Deleting all old events");
        var events = eventRepository
                .findAllByStartEventBefore(LocalDateTime.now().minusDays(1));
        log.info("Old events found: " + events.size());
        eventRepository.deleteAll(events);
        log.info("Events deleted");
    }
}
