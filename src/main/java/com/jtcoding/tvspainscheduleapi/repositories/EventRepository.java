package com.jtcoding.tvspainscheduleapi.repositories;

import com.jtcoding.tvspainscheduleapi.entities.EventEntity;
import com.jtcoding.tvspainscheduleapi.enums.EventType;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends CrudRepository<EventEntity, Long> {
  List<EventEntity> findAllByEventTypeAndEndEventGreaterThanEqualAndStartEventLessThan(
      EventType type, LocalDateTime to, LocalDateTime from);

  Page<EventEntity> findAllByEventTypeAndEndEventGreaterThanAndStartEventLessThan(
      EventType type, LocalDateTime now, LocalDateTime endOfDay, Pageable pageable);

  List<EventEntity> findAllByChannelIdAndEndEventGreaterThanAndStartEventLessThan(
          long channelId, LocalDateTime now, LocalDateTime endOfDay, Sort sort);

  Page<EventEntity> findAllByEventTypeAndStartEventGreaterThanEqualAndStartEventLessThan(
      EventType type, LocalDateTime to, LocalDateTime from, Pageable pageable);

  List<EventEntity> findAllByChannelIdAndStartEventGreaterThanEqualAndStartEventLessThan(
          long channelId, LocalDateTime to, LocalDateTime from, Sort sort);

  List<EventEntity> findAllByStartEventBefore(LocalDateTime date);
}
