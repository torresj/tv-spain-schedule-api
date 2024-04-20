package com.jtcoding.tvspainscheduleapi.services.impl;

import com.jtcoding.tvspainscheduleapi.dtos.ChannelDTO;
import com.jtcoding.tvspainscheduleapi.dtos.EventDTO;
import com.jtcoding.tvspainscheduleapi.entities.ChannelEntity;
import com.jtcoding.tvspainscheduleapi.entities.ChapterEntity;
import com.jtcoding.tvspainscheduleapi.entities.EventEntity;
import com.jtcoding.tvspainscheduleapi.exceptions.EventException;
import com.jtcoding.tvspainscheduleapi.repositories.*;
import com.jtcoding.tvspainscheduleapi.services.ChannelService;
import com.jtcoding.tvspainscheduleapi.services.EventService;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class ChannelServiceImpl implements ChannelService {

  private final ChannelRepository channelRepository;

  @Override
  public List<ChannelDTO> getChannels() {
    var channelEntities = new ArrayList<ChannelEntity>();
    channelRepository.findAll().forEach(channelEntities::add);
    return channelEntities.stream()
        .map(entity ->
                ChannelDTO.builder()
                        .logoUrl(entity.getLogoUrl())
                        .name(entity.getName()).build()
        ).toList();
  }
}
