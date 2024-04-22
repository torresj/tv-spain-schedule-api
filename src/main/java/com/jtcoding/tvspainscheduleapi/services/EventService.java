package com.jtcoding.tvspainscheduleapi.services;

import com.jtcoding.tvspainscheduleapi.dtos.EventDTO;
import com.jtcoding.tvspainscheduleapi.exceptions.EventException;

import java.util.List;

public interface EventService {
    EventDTO getEvent(long id) throws EventException;
    List<EventDTO> getTodayEventsByChannel(long channelId);
    List<EventDTO> getTomorrowEventsByChannel(long channelId);
}
