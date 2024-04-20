package com.jtcoding.tvspainscheduleapi.services;

import com.jtcoding.tvspainscheduleapi.dtos.ChannelDTO;

import java.util.List;

public interface ChannelService {
    List<ChannelDTO> getChannels();
}
