package com.jtcoding.tvspainscheduleapi.services;

import com.jtcoding.tvspainscheduleapi.dtos.ChannelDTO;
import com.jtcoding.tvspainscheduleapi.exceptions.ChannelException;

import java.util.List;

public interface ChannelService {
    List<ChannelDTO> getChannels();
    ChannelDTO getChannel(long id) throws ChannelException;
}
