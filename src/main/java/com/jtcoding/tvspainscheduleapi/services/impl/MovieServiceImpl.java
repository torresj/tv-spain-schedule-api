package com.jtcoding.tvspainscheduleapi.services.impl;

import com.jtcoding.tvspainscheduleapi.dtos.EventDTO;
import com.jtcoding.tvspainscheduleapi.enums.EventType;
import com.jtcoding.tvspainscheduleapi.repositories.EventRepository;
import com.jtcoding.tvspainscheduleapi.repositories.MovieRepository;
import com.jtcoding.tvspainscheduleapi.services.MovieService;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class MovieServiceImpl implements MovieService {
  private final MovieRepository movieRepository;
  private final EventRepository eventRepository;

  @Override
  public List<EventDTO> getLiveMovies() {
    var events =
        new java.util.ArrayList<EventDTO>(
            eventRepository
                .findAllByEventTypeAndEndEventGreaterThanEqualAndStartEventLessThan(
                    EventType.MOVIE, LocalDateTime.now(), LocalDateTime.now())
                .parallelStream()
                .map(
                    eventEntity -> {
                      var movie = movieRepository.findById(eventEntity.getContentId());
                      return movie
                          .map(
                              movieEntity ->
                                  EventDTO.builder()
                                      .start(eventEntity.getStartEvent())
                                      .end(eventEntity.getEndEvent())
                                      .eventType(eventEntity.getEventType())
                                      .duration(eventEntity.getDuration())
                                      .name(movieEntity.getName())
                                      .rate(movieEntity.getRate())
                                      .classification(movieEntity.getClassification())
                                      .director(movieEntity.getDirector())
                                      .imageUrl(movieEntity.getImageUrl())
                                      .interpreters(movieEntity.getInterpreters())
                                      .synopsis(movieEntity.getSynopsis())
                                      .build())
                          .orElse(null);
                    })
                .toList());
    events.sort(
        (event1, event2) -> {
          if (event1.getRate() == null) {
            return 1;
          }
          if (event2.getRate() == null) {
            return -1;
          }
          return event2.getRate().compareTo(event1.getRate());
        });

    return events;
  }

  @Override
  public List<EventDTO> getTodayMovies() {
    return null;
  }

  @Override
  public List<EventDTO> getTomorrowMovies() {
    return null;
  }
}
