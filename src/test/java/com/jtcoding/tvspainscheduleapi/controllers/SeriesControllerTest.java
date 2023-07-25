package com.jtcoding.tvspainscheduleapi.controllers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jtcoding.tvspainscheduleapi.dtos.EventDTO;
import com.jtcoding.tvspainscheduleapi.dtos.PageDTO;
import com.jtcoding.tvspainscheduleapi.entities.*;
import com.jtcoding.tvspainscheduleapi.enums.EventType;
import com.jtcoding.tvspainscheduleapi.repositories.*;
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
public class SeriesControllerTest {
  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
  @Autowired
  private MockMvc mockMvc;

  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
  @Autowired
  private ObjectMapper objectMapper;

  @Autowired private SerieRepository serieRepository;
  @Autowired private ChapterRepository chapterRepository;
  @Autowired private EventRepository eventRepository;
  @Autowired private ChannelRepository channelRepository;

  @Order(1)
  @Test
  @DisplayName("Get live series")
  public void getLiveSeries() throws Exception {
    var now = LocalDateTime.now(ZoneId.of("CET"));
    List<ChannelEntity> channels =
        (List<ChannelEntity>) channelRepository.saveAll(generateChannels());
    List<SerieEntity> series = (List<SerieEntity>) serieRepository.saveAll(generateSeries());
    List<ChapterEntity> chapters =
        (List<ChapterEntity>)
            chapterRepository.saveAll(
                generateChapters(
                    series.get(0).getId(),
                    series.get(1).getId(),
                    series.get(2).getId(),
                    series.get(3).getId()));
    eventRepository.saveAll(
        List.of(
            EventEntity.builder()
                .eventType(EventType.SERIE)
                .channelId(channels.get(0).getId())
                .contentId(chapters.get(0).getId())
                .startEvent(now.minusHours(1))
                .endEvent(now.plusMinutes(30))
                .build(),
            EventEntity.builder()
                .eventType(EventType.SERIE)
                .channelId(channels.get(1).getId())
                .contentId(chapters.get(1).getId())
                .startEvent(now.minusHours(1))
                .endEvent(now.plusMinutes(400))
                .build(),
            EventEntity.builder()
                .eventType(EventType.SERIE)
                .channelId(channels.get(2).getId())
                .contentId(chapters.get(2).getId())
                .startEvent(now.plusHours(2))
                .endEvent(now.plusHours(3))
                .build()));

    var result =
        mockMvc
            .perform(
                MockMvcRequestBuilders.get("/v1/series/live")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

    var content = result.andReturn().getResponse().getContentAsString();
    List<EventDTO> events = objectMapper.readValue(content, new TypeReference<List<EventDTO>>() {});

    Assertions.assertEquals(2, events.size());

    eventRepository.deleteAll();
    channelRepository.deleteAll();
    serieRepository.deleteAll();
    chapterRepository.deleteAll();
  }

  @Order(2)
  @Test
  @DisplayName("Get today series")
  public void getTodaySeries() throws Exception {
    var now = LocalDateTime.now(ZoneId.of("CET"));
    List<ChannelEntity> channels =
        (List<ChannelEntity>) channelRepository.saveAll(generateChannels());
    List<SerieEntity> series = (List<SerieEntity>) serieRepository.saveAll(generateSeries());
    List<ChapterEntity> chapters =
        (List<ChapterEntity>)
            chapterRepository.saveAll(
                generateChapters(
                    series.get(0).getId(),
                    series.get(1).getId(),
                    series.get(2).getId(),
                    series.get(3).getId()));
    eventRepository.saveAll(
        List.of(
            EventEntity.builder()
                .eventType(EventType.SERIE)
                .channelId(channels.get(0).getId())
                .contentId(chapters.get(0).getId())
                .startEvent(now.minusHours(1))
                .endEvent(now.plusMinutes(30))
                .build(),
            EventEntity.builder()
                .eventType(EventType.SERIE)
                .channelId(channels.get(1).getId())
                .contentId(chapters.get(1).getId())
                .startEvent(now.minusHours(1))
                .endEvent(now.plusMinutes(400))
                .build(),
            EventEntity.builder()
                .eventType(EventType.SERIE)
                .channelId(channels.get(2).getId())
                .contentId(chapters.get(2).getId())
                .startEvent(now.plusHours(2))
                .endEvent(now.plusHours(3))
                .build()));

    var result =
        mockMvc
            .perform(
                MockMvcRequestBuilders.get("/v1/series/today")
                    .queryParam("nPage", "0")
                    .queryParam("nElements", "10")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

    var content = result.andReturn().getResponse().getContentAsString();
    PageDTO page = objectMapper.readValue(content, PageDTO.class);

    Assertions.assertEquals(3, page.getEvents().size());

    eventRepository.deleteAll();
    channelRepository.deleteAll();
    serieRepository.deleteAll();
    chapterRepository.deleteAll();
  }

  @Order(3)
  @Test
  @DisplayName("Get tomorrow series")
  public void getTomorrowSeries() throws Exception {
    var now = LocalDateTime.now(ZoneId.of("CET"));
    List<ChannelEntity> channels =
        (List<ChannelEntity>) channelRepository.saveAll(generateChannels());
    List<SerieEntity> series = (List<SerieEntity>) serieRepository.saveAll(generateSeries());
    List<ChapterEntity> chapters =
        (List<ChapterEntity>)
            chapterRepository.saveAll(
                generateChapters(
                    series.get(0).getId(),
                    series.get(1).getId(),
                    series.get(2).getId(),
                    series.get(3).getId()));
    eventRepository.saveAll(
        List.of(
            EventEntity.builder()
                .eventType(EventType.SERIE)
                .channelId(channels.get(0).getId())
                .contentId(chapters.get(0).getId())
                .startEvent(now.plusDays(1).minusHours(1))
                .endEvent(now.plusDays(1).plusMinutes(30))
                .build(),
            EventEntity.builder()
                .eventType(EventType.SERIE)
                .channelId(channels.get(1).getId())
                .contentId(chapters.get(1).getId())
                .startEvent(now.minusHours(1))
                .endEvent(now.plusMinutes(400))
                .build(),
            EventEntity.builder()
                .eventType(EventType.SERIE)
                .channelId(channels.get(2).getId())
                .contentId(chapters.get(2).getId())
                .startEvent(now.plusHours(2))
                .endEvent(now.plusHours(3))
                .build()));

    var result =
        mockMvc
            .perform(
                MockMvcRequestBuilders.get("/v1/series/tomorrow")
                    .queryParam("nPage", "0")
                    .queryParam("nElements", "10")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

    var content = result.andReturn().getResponse().getContentAsString();
    PageDTO page = objectMapper.readValue(content, PageDTO.class);

    Assertions.assertEquals(1, page.getEvents().size());

    eventRepository.deleteAll();
    channelRepository.deleteAll();
    serieRepository.deleteAll();
    chapterRepository.deleteAll();
  }

  private List<SerieEntity> generateSeries() {
    return List.of(
        SerieEntity.builder()
            .name("Serie1")
            .classification("test")
            .rate(5D)
            .director("test")
            .interpreters("test")
            .build(),
        SerieEntity.builder()
            .name("Serie2")
            .classification("test")
            .rate(5D)
            .director("test")
            .interpreters("test")
            .build(),
        SerieEntity.builder()
            .name("Serie3")
            .classification("test")
            .rate(5D)
            .director("test")
            .interpreters("test")
            .build(),
        SerieEntity.builder()
            .name("Serie4")
            .classification("test")
            .rate(5D)
            .director("test")
            .interpreters("test")
            .build());
  }

  private List<ChapterEntity> generateChapters(long id1, long id2, long id3, long id4) {
    return List.of(
        ChapterEntity.builder().serieId(id1).chapterName("Chapter1").synopsis("").build(),
        ChapterEntity.builder().serieId(id2).chapterName("Chapter2").synopsis("").build(),
        ChapterEntity.builder().serieId(id3).chapterName("Chapter3").synopsis("").build(),
        ChapterEntity.builder().serieId(id4).chapterName("Chapter4").synopsis("").build());
  }

  private List<ChannelEntity> generateChannels() {
    return List.of(
        ChannelEntity.builder().logoUrl("").name("Channel1").build(),
        ChannelEntity.builder().logoUrl("").name("Channel2").build(),
        ChannelEntity.builder().logoUrl("").name("Channel3").build(),
        ChannelEntity.builder().logoUrl("").name("Channel4").build());
  }
}
