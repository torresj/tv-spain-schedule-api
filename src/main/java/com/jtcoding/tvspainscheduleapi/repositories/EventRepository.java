package com.jtcoding.tvspainscheduleapi.repositories;

import com.jtcoding.tvspainscheduleapi.entities.EventEntity;
import com.jtcoding.tvspainscheduleapi.enums.EventType;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends CrudRepository<EventEntity, Long> {
  List<EventEntity> findAllByEventTypeAndEndEventGreaterThanEqualAndStartEventLessThan(
      EventType type, LocalDateTime to, LocalDateTime from);
}
