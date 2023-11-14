package com.jtcoding.tvspainscheduleapi.services.impl;

import com.jtcoding.tvspainscheduleapi.dtos.ChannelDTO;
import com.jtcoding.tvspainscheduleapi.dtos.EventDTO;
import com.jtcoding.tvspainscheduleapi.dtos.PageDTO;
import com.jtcoding.tvspainscheduleapi.entities.EventEntity;
import com.jtcoding.tvspainscheduleapi.enums.EventType;
import com.jtcoding.tvspainscheduleapi.repositories.ChannelRepository;
import com.jtcoding.tvspainscheduleapi.repositories.EventRepository;
import com.jtcoding.tvspainscheduleapi.repositories.MovieRepository;
import com.jtcoding.tvspainscheduleapi.repositories.SportRepository;
import com.jtcoding.tvspainscheduleapi.services.MovieService;
import com.jtcoding.tvspainscheduleapi.services.SportsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class SportsServiceImpl implements SportsService {
    private final EventRepository eventRepository;
    private final SportRepository sportRepository;
    private final ChannelRepository channelRepository;

    @Override
    public List<EventDTO> getLiveSports() {
        return eventRepository
                .findAllByEventTypeAndEndEventGreaterThanEqualAndStartEventLessThan(
                        EventType.SPORT,
                        LocalDateTime.now(ZoneId.of("CET")),
                        LocalDateTime.now(ZoneId.of("CET")))
                .parallelStream()
                .map(this::mapEventEntityToDto)
                .toList();
    }

    @Override
    public PageDTO getTodaySports(int nPage, int nElements) {
        var pageRequest = PageRequest.of(nPage, nElements, Sort.by("startEvent").ascending());
        var now = LocalDateTime.now(ZoneId.of("CET"));
        var page =
                eventRepository.findAllByEventTypeAndEndEventGreaterThanAndStartEventLessThan(
                        EventType.SPORT,
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
    public PageDTO getTomorrowSports(int nPage, int nElements) {
        var pageRequest = PageRequest.of(nPage, nElements, Sort.by("startEvent").ascending());
        var now = LocalDateTime.now(ZoneId.of("CET"));
        var tomorrow = now.plusDays(1);
        var page =
                eventRepository.findAllByEventTypeAndStartEventGreaterThanEqualAndStartEventLessThan(
                        EventType.SPORT,
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
        var sport = sportRepository.findById(eventEntity.getContentId());
        var channel = channelRepository.findById(eventEntity.getChannelId());
        var progress = Math.round(eventEntity.getStartEvent().until(LocalDateTime.now(ZoneId.of("CET")), ChronoUnit.MINUTES) * 100.0 / eventEntity.getDuration());
        return sport
                .map(
                        sportEntity ->
                                EventDTO.builder()
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
}
