package com.jtcoding.tvspainscheduleapi.services.impl;

import com.jtcoding.tvspainscheduleapi.dtos.ChannelDTO;
import com.jtcoding.tvspainscheduleapi.dtos.EventDTO;
import com.jtcoding.tvspainscheduleapi.entities.ChapterEntity;
import com.jtcoding.tvspainscheduleapi.entities.EventEntity;
import com.jtcoding.tvspainscheduleapi.exceptions.EventException;
import com.jtcoding.tvspainscheduleapi.repositories.*;
import com.jtcoding.tvspainscheduleapi.services.EventService;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class EventServiceImpl implements EventService {
  private final MovieRepository movieRepository;
  private final EventRepository eventRepository;
  private final ChannelRepository channelRepository;
  private final ChapterRepository chapterRepository;
  private final SportRepository sportRepository;
  private final SerieRepository serieRepository;

  @Override
  public EventDTO getEvent(long id) throws EventException {
    var event = eventRepository.findById(id).orElseThrow(EventException::new);
    return switch (event.getEventType()){
        case SERIE -> mapSerieEventEntityToDto(event);
        case MOVIE -> mapMovieEventEntityToDto(event);
        case SPORT -> mapSportEventEntityToDto(event);
    };
  }

  @Override
  public List<EventDTO> getTodayEventsByChannel(long channelId) {
    var now = LocalDateTime.now(ZoneId.of("CET"));
    return eventRepository.findAllByChannelIdAndEndEventGreaterThanAndStartEventLessThan(
            channelId,
            now,
            LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 23, 59),
            Sort.by("startEvent").ascending()
    ).stream().map(event -> switch (event.getEventType()){
        case SERIE -> mapSerieEventEntityToDto(event);
        case MOVIE -> mapMovieEventEntityToDto(event);
        case SPORT -> mapSportEventEntityToDto(event);
    }).toList();
  }

  @Override
  public List<EventDTO> getTomorrowEventsByChannel(long channelId) {
    var now = LocalDateTime.now(ZoneId.of("CET"));
    var tomorrow = now.plusDays(1);
    return eventRepository.findAllByChannelIdAndStartEventGreaterThanEqualAndStartEventLessThan(
            channelId,
            LocalDateTime.of(
                    tomorrow.getYear(), tomorrow.getMonth(), tomorrow.getDayOfMonth(), 0, 0),
            LocalDateTime.of(
                    tomorrow.getYear(), tomorrow.getMonth(), tomorrow.getDayOfMonth(), 23, 59),
            Sort.by("startEvent").ascending()
    ).stream().map(event -> switch (event.getEventType()){
      case SERIE -> mapSerieEventEntityToDto(event);
      case MOVIE -> mapMovieEventEntityToDto(event);
      case SPORT -> mapSportEventEntityToDto(event);
    }).toList();
  }

  private EventDTO mapSportEventEntityToDto(EventEntity eventEntity) {
    var sport = sportRepository.findById(eventEntity.getContentId());
    var channel = channelRepository.findById(eventEntity.getChannelId());
    var progress = (int) Math.round(eventEntity.getStartEvent().until(LocalDateTime.now(ZoneId.of("CET")), ChronoUnit.MINUTES) * 100.0 / eventEntity.getDuration());
    return sport
            .map(
                    sportEntity ->
                            EventDTO.builder()
                                    .id(eventEntity.getId())
                                    .start(eventEntity.getStartEvent())
                                    .end(eventEntity.getEndEvent())
                                    .eventType(eventEntity.getEventType())
                                    .duration(eventEntity.getDuration())
                                    .progress(Math.max(progress, 0))
                                    .name(sportEntity.getName())
                                    .classification(sportEntity.getClassification())
                                    .synopsis(sportEntity.getSynopsis())
                                    .channel(
                                            channel
                                                    .map(
                                                            channelEntity ->
                                                                    ChannelDTO.builder()
                                                                            .id(channelEntity.getId())
                                                                            .logoUrl(channelEntity.getLogoUrl())
                                                                            .name(channelEntity.getName())
                                                                            .build())
                                                    .orElse(null))
                                    .build())
            .orElse(null);
  }

  private EventDTO mapSerieEventEntityToDto(EventEntity eventEntity) {
    var channel = channelRepository.findById(eventEntity.getChannelId());
    var chapter = chapterRepository.findById(eventEntity.getContentId());
    var serie =
            chapter
                    .map(chapterEntity -> serieRepository.findById(chapterEntity.getSerieId()))
                    .orElse(null);
    var progress = (int) Math.round(eventEntity.getStartEvent().until(LocalDateTime.now(ZoneId.of("CET")), ChronoUnit.MINUTES) * 100.0 / eventEntity.getDuration());
    return serie
            .map(
                    serieEntity ->
                            EventDTO.builder()
                                    .id(eventEntity.getId())
                                    .start(eventEntity.getStartEvent())
                                    .end(eventEntity.getEndEvent())
                                    .eventType(eventEntity.getEventType())
                                    .duration(eventEntity.getDuration())
                                    .progress(Math.max(progress, 0))
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
                                                                            .id(channelEntity.getId())
                                                                            .logoUrl(channelEntity.getLogoUrl())
                                                                            .name(channelEntity.getName())
                                                                            .build())
                                                    .orElse(null))
                                    .build())
            .orElse(null);
  }

  private EventDTO mapMovieEventEntityToDto(EventEntity eventEntity) {
    var movie = movieRepository.findById(eventEntity.getContentId());
    var channel = channelRepository.findById(eventEntity.getChannelId());
    var progress = (int) Math.round(eventEntity.getStartEvent().until(LocalDateTime.now(ZoneId.of("CET")), ChronoUnit.MINUTES) * 100.0 / eventEntity.getDuration());
    return movie
        .map(
            movieEntity ->
                EventDTO.builder()
                        .id(eventEntity.getId())
                    .start(eventEntity.getStartEvent())
                    .end(eventEntity.getEndEvent())
                    .eventType(eventEntity.getEventType())
                    .duration(eventEntity.getDuration())
                        .progress(Math.max(progress, 0))
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
                                    ChannelDTO.builder().id(channelEntity.getId())
                                        .logoUrl(channelEntity.getLogoUrl())
                                        .name(channelEntity.getName())
                                        .build())
                            .orElse(null))
                    .build())
        .orElse(null);
  }
}
