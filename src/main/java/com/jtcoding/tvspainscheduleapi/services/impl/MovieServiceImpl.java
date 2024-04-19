package com.jtcoding.tvspainscheduleapi.services.impl;

import com.jtcoding.tvspainscheduleapi.dtos.ChannelDTO;
import com.jtcoding.tvspainscheduleapi.dtos.EventDTO;
import com.jtcoding.tvspainscheduleapi.dtos.PageDTO;
import com.jtcoding.tvspainscheduleapi.entities.EventEntity;
import com.jtcoding.tvspainscheduleapi.enums.EventType;
import com.jtcoding.tvspainscheduleapi.repositories.ChannelRepository;
import com.jtcoding.tvspainscheduleapi.repositories.EventRepository;
import com.jtcoding.tvspainscheduleapi.repositories.MovieRepository;
import com.jtcoding.tvspainscheduleapi.services.MovieService;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class MovieServiceImpl implements MovieService {
  private final MovieRepository movieRepository;
  private final EventRepository eventRepository;
  private final ChannelRepository channelRepository;

  @Override
  public List<EventDTO> getLiveMovies() {
    var events =
        new java.util.ArrayList<>(
            eventRepository
                .findAllByEventTypeAndEndEventGreaterThanEqualAndStartEventLessThan(
                    EventType.MOVIE,
                    LocalDateTime.now(ZoneId.of("CET")),
                    LocalDateTime.now(ZoneId.of("CET")))
                .parallelStream()
                .map(this::mapEventEntityToDto)
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
  public PageDTO getTodayMovies(int nPage, int nElements) {
    var pageRequest = PageRequest.of(nPage, nElements, Sort.by("startEvent").ascending());
    var now = LocalDateTime.now(ZoneId.of("CET"));
    var page =
        eventRepository.findAllByEventTypeAndEndEventGreaterThanAndStartEventLessThan(
            EventType.MOVIE,
            now,
            LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 23, 59),
            pageRequest);
    return PageDTO.builder()
        .page(page.getPageable().getPageNumber())
        .elements(page.getContent().size())
        .totalElements(page.getTotalElements())
        .totalPages(page.getTotalPages())
        .events(page.getContent().parallelStream().map(this::mapEventEntityToDto).toList())
        .build();
  }

  @Override
  public PageDTO getTomorrowMovies(int nPage, int nElements) {
    var pageRequest = PageRequest.of(nPage, nElements, Sort.by("startEvent").ascending());
    var now = LocalDateTime.now(ZoneId.of("CET"));
    var tomorrow = now.plusDays(1);
    var page =
        eventRepository.findAllByEventTypeAndStartEventGreaterThanEqualAndStartEventLessThan(
            EventType.MOVIE,
            LocalDateTime.of(
                tomorrow.getYear(), tomorrow.getMonth(), tomorrow.getDayOfMonth(), 0, 0),
            LocalDateTime.of(
                tomorrow.getYear(), tomorrow.getMonth(), tomorrow.getDayOfMonth(), 23, 59),
            pageRequest);
    return PageDTO.builder()
        .page(page.getPageable().getPageNumber())
        .elements(page.getContent().size())
        .totalElements(page.getTotalElements())
        .totalPages(page.getTotalPages())
        .events(page.getContent().parallelStream().map(this::mapEventEntityToDto).toList())
        .build();
  }

  private EventDTO mapEventEntityToDto(EventEntity eventEntity) {
    var movie = movieRepository.findById(eventEntity.getContentId());
    var channel = channelRepository.findById(eventEntity.getChannelId());
    var progress = Math.round(eventEntity.getStartEvent().until(LocalDateTime.now(ZoneId.of("CET")), ChronoUnit.MINUTES) * 100.0 / eventEntity.getDuration());
    return movie
        .map(
            movieEntity ->
                EventDTO.builder()
                        .id(eventEntity.getId())
                    .start(eventEntity.getStartEvent())
                    .end(eventEntity.getEndEvent())
                    .eventType(eventEntity.getEventType())
                    .duration(eventEntity.getDuration())
                        .progress((int) progress)
                    .name(movieEntity.getName())
                    .rate(movieEntity.getRate())
                    .classification(movieEntity.getClassification())
                    .director(movieEntity.getDirector())
                    .imageUrl(movieEntity.getImageUrl())
                    .interpreters(movieEntity.getInterpreters())
                    .synopsis(movieEntity.getSynopsis())
                    .channel(
                        channel
                            .map(
                                channelEntity ->
                                    ChannelDTO.builder()
                                        .logoUrl(channelEntity.getLogoUrl())
                                        .name(channelEntity.getName())
                                        .build())
                            .orElse(null))
                    .build())
        .orElse(null);
  }
}
