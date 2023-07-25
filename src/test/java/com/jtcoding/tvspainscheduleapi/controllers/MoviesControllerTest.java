package com.jtcoding.tvspainscheduleapi.controllers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jtcoding.tvspainscheduleapi.dtos.EventDTO;
import com.jtcoding.tvspainscheduleapi.dtos.PageDTO;
import com.jtcoding.tvspainscheduleapi.entities.ChannelEntity;
import com.jtcoding.tvspainscheduleapi.entities.EventEntity;
import com.jtcoding.tvspainscheduleapi.entities.MovieEntity;
import com.jtcoding.tvspainscheduleapi.enums.EventType;
import com.jtcoding.tvspainscheduleapi.repositories.ChannelRepository;
import com.jtcoding.tvspainscheduleapi.repositories.EventRepository;
import com.jtcoding.tvspainscheduleapi.repositories.MovieRepository;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MoviesControllerTest {
  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
  @Autowired
  private MockMvc mockMvc;

  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
  @Autowired
  private ObjectMapper objectMapper;

  @Autowired private MovieRepository movieRepository;
  @Autowired private EventRepository eventRepository;
  @Autowired private ChannelRepository channelRepository;

  @Order(1)
  @Test
  @DisplayName("Get live movies")
  public void getLiveMovies() throws Exception {
    var now = LocalDateTime.now(ZoneId.of("CET"));
    List<ChannelEntity> channels = (List<ChannelEntity>) channelRepository.saveAll(generateChannels());
    List<MovieEntity> movies = (List<MovieEntity>) movieRepository.saveAll(generateMovies());
    eventRepository.saveAll(
        List.of(
            EventEntity.builder()
                .eventType(EventType.MOVIE)
                .channelId(channels.get(0).getId())
                .contentId(movies.get(0).getId())
                .startEvent(now.minusHours(1))
                .endEvent(now.plusMinutes(30))
                .build(),
            EventEntity.builder()
                .eventType(EventType.MOVIE)
                .channelId(channels.get(1).getId())
                .contentId(movies.get(1).getId())
                .startEvent(now.minusHours(1))
                .endEvent(now.plusMinutes(400))
                .build(),
            EventEntity.builder()
                .eventType(EventType.MOVIE)
                .channelId(channels.get(2).getId())
                .contentId(movies.get(2).getId())
                .startEvent(now.plusHours(2))
                .endEvent(now.plusHours(3))
                .build()));

    var result =
        mockMvc
            .perform(
                MockMvcRequestBuilders.get("/v1/movies/live")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

    var content = result.andReturn().getResponse().getContentAsString();
    List<EventDTO> events = objectMapper.readValue(content, new TypeReference<List<EventDTO>>() {});

    Assertions.assertEquals(2, events.size());

    eventRepository.deleteAll();
    channelRepository.deleteAll();
    movieRepository.deleteAll();
  }

  @Order(2)
  @Test
  @DisplayName("Get today movies")
  public void getTodayMovies() throws Exception {
    var now = LocalDateTime.now(ZoneId.of("CET"));
    List<ChannelEntity> channels = (List<ChannelEntity>) channelRepository.saveAll(generateChannels());
    List<MovieEntity> movies = (List<MovieEntity>) movieRepository.saveAll(generateMovies());
    eventRepository.saveAll(
            List.of(
                    EventEntity.builder()
                            .eventType(EventType.MOVIE)
                            .channelId(channels.get(0).getId())
                            .contentId(movies.get(0).getId())
                            .startEvent(now.minusHours(1))
                            .endEvent(now.plusMinutes(30))
                            .build(),
                    EventEntity.builder()
                            .eventType(EventType.MOVIE)
                            .channelId(channels.get(1).getId())
                            .contentId(movies.get(1).getId())
                            .startEvent(now.minusHours(1))
                            .endEvent(now.plusMinutes(400))
                            .build(),
                    EventEntity.builder()
                            .eventType(EventType.MOVIE)
                            .channelId(channels.get(2).getId())
                            .contentId(movies.get(2).getId())
                            .startEvent(now.plusHours(2))
                            .endEvent(now.plusHours(3))
                            .build()));

    var result =
        mockMvc
            .perform(
                MockMvcRequestBuilders.get("/v1/movies/today")
                    .queryParam("nPage", "0")
                    .queryParam("nElements", "10")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

    var content = result.andReturn().getResponse().getContentAsString();
    PageDTO page = objectMapper.readValue(content, PageDTO.class);

    Assertions.assertEquals(3, page.getEvents().size());

    eventRepository.deleteAll();
    channelRepository.deleteAll();
    movieRepository.deleteAll();
  }

  @Order(3)
  @Test
  @DisplayName("Get tomorrow movies")
  public void getTomorrowMovies() throws Exception {
    var now = LocalDateTime.now(ZoneId.of("CET"));
    List<ChannelEntity> channels = (List<ChannelEntity>) channelRepository.saveAll(generateChannels());
    List<MovieEntity> movies = (List<MovieEntity>) movieRepository.saveAll(generateMovies());
    eventRepository.saveAll(
            List.of(
                    EventEntity.builder()
                            .eventType(EventType.MOVIE)
                            .channelId(channels.get(0).getId())
                            .contentId(movies.get(0).getId())
                            .startEvent(now.plusDays(1).minusHours(1))
                            .endEvent(now.plusDays(1).plusMinutes(30))
                            .build(),
                    EventEntity.builder()
                            .eventType(EventType.MOVIE)
                            .channelId(channels.get(1).getId())
                            .contentId(movies.get(1).getId())
                            .startEvent(now.minusHours(1))
                            .endEvent(now.plusMinutes(400))
                            .build(),
                    EventEntity.builder()
                            .eventType(EventType.MOVIE)
                            .channelId(channels.get(2).getId())
                            .contentId(movies.get(2).getId())
                            .startEvent(now.plusHours(2))
                            .endEvent(now.plusHours(3))
                            .build()));

    var result =
            mockMvc
                    .perform(
                            MockMvcRequestBuilders.get("/v1/movies/tomorrow")
                                    .queryParam("nPage", "0")
                                    .queryParam("nElements", "10")
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());

    var content = result.andReturn().getResponse().getContentAsString();
    PageDTO page = objectMapper.readValue(content, PageDTO.class);

    Assertions.assertEquals(1, page.getEvents().size());

    eventRepository.deleteAll();
    channelRepository.deleteAll();
    movieRepository.deleteAll();
  }

  private List<MovieEntity> generateMovies() {
    return List.of(
        MovieEntity.builder()
            .name("Movie1")
            .classification("test")
            .rate(5D)
            .director("test")
            .interpreters("test")
            .synopsis("test")
            .build(),
        MovieEntity.builder()
            .name("Movie2")
            .classification("test")
            .rate(5D)
            .director("test")
            .interpreters("test")
            .synopsis("test")
            .build(),
        MovieEntity.builder()
            .name("Movie3")
            .classification("test")
            .rate(5D)
            .director("test")
            .interpreters("test")
            .synopsis("test")
            .build(),
        MovieEntity.builder()
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
        ChannelEntity.builder().logoUrl("").name("Channel1").build(),
        ChannelEntity.builder().logoUrl("").name("Channel2").build(),
        ChannelEntity.builder().logoUrl("").name("Channel3").build(),
        ChannelEntity.builder().logoUrl("").name("Channel4").build());
  }
}
