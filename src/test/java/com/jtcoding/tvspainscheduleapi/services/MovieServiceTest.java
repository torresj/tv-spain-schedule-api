package com.jtcoding.tvspainscheduleapi.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.jtcoding.tvspainscheduleapi.entities.ChannelEntity;
import com.jtcoding.tvspainscheduleapi.entities.EventEntity;
import com.jtcoding.tvspainscheduleapi.entities.MovieEntity;
import com.jtcoding.tvspainscheduleapi.enums.EventType;
import com.jtcoding.tvspainscheduleapi.repositories.ChannelRepository;
import com.jtcoding.tvspainscheduleapi.repositories.EventRepository;
import com.jtcoding.tvspainscheduleapi.repositories.MovieRepository;
import com.jtcoding.tvspainscheduleapi.services.impl.MovieServiceImpl;
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
class MovieServiceTest {

  @Mock MovieRepository movieRepository;
  @Mock EventRepository eventRepository;
  @Mock ChannelRepository channelRepository;

  @InjectMocks MovieServiceImpl movieService;

  @Test
  @DisplayName("Get live movies")
  void getLiveMovies() {
    List<MovieEntity> movies = generateMovies();
    List<ChannelEntity> channels = generateChannels();
    var now = LocalDateTime.now(ZoneId.of("CET"));
    var entityEvents =
        List.of(
            EventEntity.builder()
                .id(1L)
                .eventType(EventType.MOVIE)
                .channelId(1L)
                .contentId(1L)
                .startEvent(now.minusHours(1))
                .endEvent(now.plusMinutes(30))
                .build(),
            EventEntity.builder()
                .id(2L)
                .eventType(EventType.MOVIE)
                .channelId(2L)
                .contentId(2L)
                .startEvent(now.minusHours(1))
                .endEvent(now.plusMinutes(400))
                .build(),
            EventEntity.builder()
                .id(3L)
                .eventType(EventType.MOVIE)
                .channelId(3L)
                .contentId(3L)
                .startEvent(now.minusHours(2))
                .endEvent(now.plusHours(3))
                .build());

    when(movieRepository.findById(1L)).thenReturn(Optional.of(movies.get(0)));
    when(movieRepository.findById(2L)).thenReturn(Optional.of(movies.get(1)));
    when(movieRepository.findById(3L)).thenReturn(Optional.of(movies.get(2)));

    when(channelRepository.findById(1L)).thenReturn(Optional.of(channels.get(0)));
    when(channelRepository.findById(2L)).thenReturn(Optional.of(channels.get(1)));
    when(channelRepository.findById(3L)).thenReturn(Optional.of(channels.get(2)));

    when(eventRepository.findAllByEventTypeAndEndEventGreaterThanEqualAndStartEventLessThan(
            any(), any(), any()))
        .thenReturn(entityEvents);

    var events = movieService.getLiveMovies();

    Assertions.assertEquals(3, events.size());
    Assertions.assertEquals(entityEvents.get(1).getStartEvent(), events.get(0).getStart());
    Assertions.assertEquals(entityEvents.get(0).getStartEvent(), events.get(1).getStart());
    Assertions.assertEquals(entityEvents.get(2).getStartEvent(), events.get(2).getStart());
  }

  @Test
  @DisplayName("Get today movies")
  void getTodayMovies() {
    List<MovieEntity> movies = generateMovies();
    List<ChannelEntity> channels = generateChannels();
    var now = LocalDateTime.now(ZoneId.of("CET"));
    var entityEvents =
        List.of(
            EventEntity.builder()
                .id(1L)
                .eventType(EventType.MOVIE)
                .channelId(1L)
                .contentId(1L)
                .startEvent(now.minusHours(1))
                .endEvent(now.plusMinutes(30))
                .build(),
            EventEntity.builder()
                .id(2L)
                .eventType(EventType.MOVIE)
                .channelId(2L)
                .contentId(2L)
                .startEvent(now.minusHours(1))
                .endEvent(now.plusMinutes(400))
                .build(),
            EventEntity.builder()
                .id(3L)
                .eventType(EventType.MOVIE)
                .channelId(3L)
                .contentId(3L)
                .startEvent(now.minusHours(2))
                .endEvent(now.plusHours(3))
                .build());

    when(movieRepository.findById(1L)).thenReturn(Optional.of(movies.get(0)));
    when(movieRepository.findById(2L)).thenReturn(Optional.of(movies.get(1)));
    when(movieRepository.findById(3L)).thenReturn(Optional.of(movies.get(2)));

    when(channelRepository.findById(1L)).thenReturn(Optional.of(channels.get(0)));
    when(channelRepository.findById(2L)).thenReturn(Optional.of(channels.get(1)));
    when(channelRepository.findById(3L)).thenReturn(Optional.of(channels.get(2)));

    when(eventRepository.findAllByEventTypeAndEndEventGreaterThanAndStartEventLessThan(
            any(), any(), any(), any()))
        .thenReturn(new PageImpl<>(entityEvents, Pageable.ofSize(3), 3));

    var page = movieService.getTodayMovies(0, 20);

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
    List<MovieEntity> movies = generateMovies();
    List<ChannelEntity> channels = generateChannels();
    var now = LocalDateTime.now(ZoneId.of("CET"));
    var entityEvents =
        List.of(
            EventEntity.builder()
                .id(1L)
                .eventType(EventType.MOVIE)
                .channelId(1L)
                .contentId(1L)
                .startEvent(now.minusHours(1))
                .endEvent(now.plusMinutes(30))
                .build(),
            EventEntity.builder()
                .id(2L)
                .eventType(EventType.MOVIE)
                .channelId(2L)
                .contentId(2L)
                .startEvent(now.minusHours(1))
                .endEvent(now.plusMinutes(400))
                .build(),
            EventEntity.builder()
                .id(3L)
                .eventType(EventType.MOVIE)
                .channelId(3L)
                .contentId(3L)
                .startEvent(now.minusHours(2))
                .endEvent(now.plusHours(3))
                .build());

    when(movieRepository.findById(1L)).thenReturn(Optional.of(movies.get(0)));
    when(movieRepository.findById(2L)).thenReturn(Optional.of(movies.get(1)));
    when(movieRepository.findById(3L)).thenReturn(Optional.of(movies.get(2)));

    when(channelRepository.findById(1L)).thenReturn(Optional.of(channels.get(0)));
    when(channelRepository.findById(2L)).thenReturn(Optional.of(channels.get(1)));
    when(channelRepository.findById(3L)).thenReturn(Optional.of(channels.get(2)));

    when(eventRepository.findAllByEventTypeAndStartEventGreaterThanEqualAndStartEventLessThan(
            any(), any(), any(), any()))
        .thenReturn(new PageImpl<>(entityEvents, Pageable.ofSize(3), 3));

    var page = movieService.getTomorrowMovies(0, 20);

    Assertions.assertEquals(3, page.getEvents().size());
    Assertions.assertEquals(
        entityEvents.get(1).getStartEvent(), page.getEvents().get(0).getStart());
    Assertions.assertEquals(
        entityEvents.get(0).getStartEvent(), page.getEvents().get(1).getStart());
    Assertions.assertEquals(
        entityEvents.get(2).getStartEvent(), page.getEvents().get(2).getStart());
  }

  private List<MovieEntity> generateMovies() {
    return List.of(
        MovieEntity.builder()
            .id(1L)
            .name("Movie1")
            .classification("test")
            .rate(5D)
            .director("test")
            .interpreters("test")
            .synopsis("test")
            .build(),
        MovieEntity.builder()
            .id(2L)
            .name("Movie2")
            .classification("test")
            .rate(5D)
            .director("test")
            .interpreters("test")
            .synopsis("test")
            .build(),
        MovieEntity.builder()
            .id(3L)
            .name("Movie3")
            .classification("test")
            .rate(5D)
            .director("test")
            .interpreters("test")
            .synopsis("test")
            .build(),
        MovieEntity.builder()
            .id(4L)
            .name("Movie4")
            .classification("test")
            .rate(5D)
            .director("test")
            .interpreters("test")
            .synopsis("test")
            .build());
  }

  private List<ChannelEntity> generateChannels() {
    return List.of(
        ChannelEntity.builder().id(1L).logoUrl("").name("Channel1").build(),
        ChannelEntity.builder().id(2L).logoUrl("").name("Channel2").build(),
        ChannelEntity.builder().id(3L).logoUrl("").name("Channel3").build(),
        ChannelEntity.builder().id(4L).logoUrl("").name("Channel4").build());
  }
}
