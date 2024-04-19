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
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

  private EventDTO mapSportEventEntityToDto(EventEntity eventEntity) {
    var sport = sportRepository.findById(eventEntity.getContentId());
    var channel = channelRepository.findById(eventEntity.getChannelId());
    var progress = Math.round(eventEntity.getStartEvent().until(LocalDateTime.now(ZoneId.of("CET")), ChronoUnit.MINUTES) * 100.0 / eventEntity.getDuration());
    return sport
            .map(
                    sportEntity ->
                            EventDTO.builder()
                                    .id(eventEntity.getId())
                                    .start(eventEntity.getStartEvent())
                                    .end(eventEntity.getEndEvent())
                                    .eventType(eventEntity.getEventType())
                                    .duration(eventEntity.getDuration())
                                    .progress((int) progress)
                                    .name(sportEntity.getName())
                                    .classification(sportEntity.getClassification())
                                    .synopsis(sportEntity.getSynopsis())
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

  private EventDTO mapSerieEventEntityToDto(EventEntity eventEntity) {
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
                                    .id(eventEntity.getId())
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

  private EventDTO mapMovieEventEntityToDto(EventEntity eventEntity) {
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
