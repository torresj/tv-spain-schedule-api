package com.jtcoding.tvspainscheduleapi.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.jtcoding.tvspainscheduleapi.entities.*;
import com.jtcoding.tvspainscheduleapi.enums.EventType;
import com.jtcoding.tvspainscheduleapi.repositories.*;
import com.jtcoding.tvspainscheduleapi.services.impl.SerieServiceImpl;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class SerieServiceTest {

  @Mock SerieRepository serieRepository;
  @Mock EventRepository eventRepository;
  @Mock ChannelRepository channelRepository;
  @Mock ChapterRepository chapterRepository;

  @InjectMocks SerieServiceImpl serieService;

  @Test
  @DisplayName("Get live series")
  void getLiveSeries() {
    List<SerieEntity> series = generateSeries();
    List<ChannelEntity> channels = generateChannels();
    List<ChapterEntity> chapters = generateChapters();
    var now = LocalDateTime.now(ZoneId.of("CET"));
    var entityEvents =
        List.of(
            EventEntity.builder()
                .id(1L)
                .eventType(EventType.SERIE)
                .channelId(1L)
                .contentId(1L)
                .startEvent(now.minusHours(1))
                .endEvent(now.plusMinutes(30))
                .build(),
            EventEntity.builder()
                .id(2L)
                .eventType(EventType.SERIE)
                .channelId(2L)
                .contentId(2L)
                .startEvent(now.minusHours(1))
                .endEvent(now.plusMinutes(400))
                .build(),
            EventEntity.builder()
                .id(3L)
                .eventType(EventType.SERIE)
                .channelId(3L)
                .contentId(3L)
                .startEvent(now.minusHours(2))
                .endEvent(now.plusHours(3))
                .build());

    when(serieRepository.findById(1L)).thenReturn(Optional.of(series.get(0)));
    when(serieRepository.findById(2L)).thenReturn(Optional.of(series.get(1)));
    when(serieRepository.findById(3L)).thenReturn(Optional.of(series.get(2)));

    when(channelRepository.findById(1L)).thenReturn(Optional.of(channels.get(0)));
    when(channelRepository.findById(2L)).thenReturn(Optional.of(channels.get(1)));
    when(channelRepository.findById(3L)).thenReturn(Optional.of(channels.get(2)));

    when(chapterRepository.findById(1L)).thenReturn(Optional.of(chapters.get(0)));
    when(chapterRepository.findById(2L)).thenReturn(Optional.of(chapters.get(1)));
    when(chapterRepository.findById(3L)).thenReturn(Optional.of(chapters.get(2)));

    when(eventRepository.findAllByEventTypeAndEndEventGreaterThanEqualAndStartEventLessThan(
            any(), any(), any()))
        .thenReturn(entityEvents);

    var events = serieService.getLiveSeries();

    Assertions.assertEquals(3, events.size());
    Assertions.assertEquals(entityEvents.get(1).getStartEvent(), events.get(0).getStart());
    Assertions.assertEquals(entityEvents.get(0).getStartEvent(), events.get(1).getStart());
    Assertions.assertEquals(entityEvents.get(2).getStartEvent(), events.get(2).getStart());
  }

  @Test
  @DisplayName("Get today movies")
  void getTodayMovies() {
    List<SerieEntity> series = generateSeries();
    List<ChannelEntity> channels = generateChannels();
    List<ChapterEntity> chapters = generateChapters();
    var now = LocalDateTime.now(ZoneId.of("CET"));
    var entityEvents =
            List.of(
                    EventEntity.builder()
                            .id(1L)
                            .eventType(EventType.SERIE)
                            .channelId(1L)
                            .contentId(1L)
                            .startEvent(now.minusHours(1))
                            .endEvent(now.plusMinutes(30))
                            .build(),
                    EventEntity.builder()
                            .id(2L)
                            .eventType(EventType.SERIE)
                            .channelId(2L)
                            .contentId(2L)
                            .startEvent(now.minusHours(1))
                            .endEvent(now.plusMinutes(400))
                            .build(),
                    EventEntity.builder()
                            .id(3L)
                            .eventType(EventType.SERIE)
                            .channelId(3L)
                            .contentId(3L)
                            .startEvent(now.minusHours(2))
                            .endEvent(now.plusHours(3))
                            .build());

    when(serieRepository.findById(1L)).thenReturn(Optional.of(series.get(0)));
    when(serieRepository.findById(2L)).thenReturn(Optional.of(series.get(1)));
    when(serieRepository.findById(3L)).thenReturn(Optional.of(series.get(2)));

    when(channelRepository.findById(1L)).thenReturn(Optional.of(channels.get(0)));
    when(channelRepository.findById(2L)).thenReturn(Optional.of(channels.get(1)));
    when(channelRepository.findById(3L)).thenReturn(Optional.of(channels.get(2)));

    when(chapterRepository.findById(1L)).thenReturn(Optional.of(chapters.get(0)));
    when(chapterRepository.findById(2L)).thenReturn(Optional.of(chapters.get(1)));
    when(chapterRepository.findById(3L)).thenReturn(Optional.of(chapters.get(2)));

    when(eventRepository.findAllByEventTypeAndEndEventGreaterThanAndStartEventLessThan(
            any(), any(), any(), any()))
        .thenReturn(new PageImpl<>(entityEvents, Pageable.ofSize(3), 3));

    var page = serieService.getTodaySeries(0, 20);

    Assertions.assertEquals(3, page.getEvents().size());
    Assertions.assertEquals(
        entityEvents.get(1).getStartEvent(), page.getEvents().get(0).getStart());
    Assertions.assertEquals(
        entityEvents.get(0).getStartEvent(), page.getEvents().get(1).getStart());
    Assertions.assertEquals(
        entityEvents.get(2).getStartEvent(), page.getEvents().get(2).getStart());
  }

  @Test
  @DisplayName("Get tomorrow movies")
  void getTomorrowMovies() {
    List<SerieEntity> series = generateSeries();
    List<ChannelEntity> channels = generateChannels();
    List<ChapterEntity> chapters = generateChapters();
    var now = LocalDateTime.now(ZoneId.of("CET"));
    var entityEvents =
            List.of(
                    EventEntity.builder()
                            .id(1L)
                            .eventType(EventType.SERIE)
                            .channelId(1L)
                            .contentId(1L)
                            .startEvent(now.minusHours(1))
                            .endEvent(now.plusMinutes(30))
                            .build(),
                    EventEntity.builder()
                            .id(2L)
                            .eventType(EventType.SERIE)
                            .channelId(2L)
                            .contentId(2L)
                            .startEvent(now.minusHours(1))
                            .endEvent(now.plusMinutes(400))
                            .build(),
                    EventEntity.builder()
                            .id(3L)
                            .eventType(EventType.SERIE)
                            .channelId(3L)
                            .contentId(3L)
                            .startEvent(now.minusHours(2))
                            .endEvent(now.plusHours(3))
                            .build());

    when(serieRepository.findById(1L)).thenReturn(Optional.of(series.get(0)));
    when(serieRepository.findById(2L)).thenReturn(Optional.of(series.get(1)));
    when(serieRepository.findById(3L)).thenReturn(Optional.of(series.get(2)));

    when(channelRepository.findById(1L)).thenReturn(Optional.of(channels.get(0)));
    when(channelRepository.findById(2L)).thenReturn(Optional.of(channels.get(1)));
    when(channelRepository.findById(3L)).thenReturn(Optional.of(channels.get(2)));

    when(chapterRepository.findById(1L)).thenReturn(Optional.of(chapters.get(0)));
    when(chapterRepository.findById(2L)).thenReturn(Optional.of(chapters.get(1)));
    when(chapterRepository.findById(3L)).thenReturn(Optional.of(chapters.get(2)));

    when(eventRepository.findAllByEventTypeAndStartEventGreaterThanEqualAndStartEventLessThan(
            any(), any(), any(), any()))
        .thenReturn(new PageImpl<>(entityEvents, Pageable.ofSize(3), 3));

    var page = serieService.getTomorrowSeries(0, 20);

    Assertions.assertEquals(3, page.getEvents().size());
    Assertions.assertEquals(
        entityEvents.get(1).getStartEvent(), page.getEvents().get(0).getStart());
    Assertions.assertEquals(
        entityEvents.get(0).getStartEvent(), page.getEvents().get(1).getStart());
    Assertions.assertEquals(
        entityEvents.get(2).getStartEvent(), page.getEvents().get(2).getStart());
  }

  private List<SerieEntity> generateSeries() {
    return List.of(
        SerieEntity.builder()
            .id(1L)
            .name("Serie1")
            .classification("test")
            .rate(5D)
            .director("test")
            .interpreters("test")
            .build(),
        SerieEntity.builder()
            .id(2L)
            .name("Serie2")
            .classification("test")
            .rate(5D)
            .director("test")
            .interpreters("test")
            .build(),
        SerieEntity.builder()
            .id(3L)
            .name("Serie3")
            .classification("test")
            .rate(5D)
            .director("test")
            .interpreters("test")
            .build(),
        SerieEntity.builder()
            .id(4L)
            .name("Serie4")
            .classification("test")
            .rate(5D)
            .director("test")
            .interpreters("test")
            .build());
  }

  private List<ChapterEntity> generateChapters() {
    return List.of(
        ChapterEntity.builder().id(1L).serieId(1L).chapterName("Chapter1").synopsis("").build(),
        ChapterEntity.builder().id(2L).serieId(2L).chapterName("Chapter2").synopsis("").build(),
        ChapterEntity.builder().id(3L).serieId(3L).chapterName("Chapter3").synopsis("").build(),
        ChapterEntity.builder().id(4L).serieId(4L).chapterName("Chapter4").synopsis("").build());
  }

  private List<ChannelEntity> generateChannels() {
    return List.of(
        ChannelEntity.builder().id(1L).logoUrl("").name("Channel1").build(),
        ChannelEntity.builder().id(2L).logoUrl("").name("Channel2").build(),
        ChannelEntity.builder().id(3L).logoUrl("").name("Channel3").build(),
        ChannelEntity.builder().id(4L).logoUrl("").name("Channel4").build());
  }
}
