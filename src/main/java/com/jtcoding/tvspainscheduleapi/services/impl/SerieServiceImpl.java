package com.jtcoding.tvspainscheduleapi.services.impl;

import com.jtcoding.tvspainscheduleapi.dtos.ChannelDTO;
import com.jtcoding.tvspainscheduleapi.dtos.EventDTO;
import com.jtcoding.tvspainscheduleapi.dtos.PageDTO;
import com.jtcoding.tvspainscheduleapi.entities.ChapterEntity;
import com.jtcoding.tvspainscheduleapi.entities.EventEntity;
import com.jtcoding.tvspainscheduleapi.enums.EventType;
import com.jtcoding.tvspainscheduleapi.repositories.*;
import com.jtcoding.tvspainscheduleapi.services.SerieService;
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
public class SerieServiceImpl implements SerieService {
  private final SerieRepository serieRepository;
  private final ChapterRepository chapterRepository;
  private final EventRepository eventRepository;
  private final ChannelRepository channelRepository;

  @Override
  public List<EventDTO> getLiveSeries() {
    var events =
        new java.util.ArrayList<>(
            eventRepository
                .findAllByEventTypeAndEndEventGreaterThanEqualAndStartEventLessThan(
                    EventType.SERIE,
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
  public PageDTO getTodaySeries(int nPage, int nElements) {
    var pageRequest = PageRequest.of(nPage, nElements, Sort.by("startEvent").ascending());
    var now = LocalDateTime.now(ZoneId.of("CET"));
    var page =
        eventRepository.findAllByEventTypeAndEndEventGreaterThanAndStartEventLessThan(
            EventType.SERIE,
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
  public PageDTO getTomorrowSeries(int nPage, int nElements) {
    var pageRequest = PageRequest.of(nPage, nElements, Sort.by("startEvent").ascending());
    var now = LocalDateTime.now(ZoneId.of("CET"));
    var tomorrow = now.plusDays(1);
    var page =
        eventRepository.findAllByEventTypeAndStartEventGreaterThanEqualAndStartEventLessThan(
            EventType.SERIE,
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
    var channel = channelRepository.findById(eventEntity.getChannelId());
    var chapter = chapterRepository.findById(eventEntity.getContentId());
    var serie =
        chapter
            .map(chapterEntity -> serieRepository.findById(chapterEntity.getSerieId()))
            .orElse(null);
    var progress = Math.round(eventEntity.getStartEvent().until(LocalDateTime.now(ZoneId.of("CET")), ChronoUnit.MINUTES) * 100.0 / eventEntity.getDuration());
    return serie
        .map(
            serieEntity ->
                EventDTO.builder()
                    .start(eventEntity.getStartEvent())
                    .end(eventEntity.getEndEvent())
                    .eventType(eventEntity.getEventType())
                    .duration(eventEntity.getDuration())
                        .progress((int) progress)
                    .name(serieEntity.getName())
                    .rate(serieEntity.getRate())
                    .classification(serieEntity.getClassification())
                    .director(serieEntity.getDirector())
                    .imageUrl(serieEntity.getImageUrl())
                    .interpreters(serieEntity.getInterpreters())
                    .synopsis(chapter.map(ChapterEntity::getSynopsis).orElse(null))
                    .chapterName(chapter.map(ChapterEntity::getChapterName).orElse(null))
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
