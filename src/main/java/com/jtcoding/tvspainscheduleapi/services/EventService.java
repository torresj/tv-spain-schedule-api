package com.jtcoding.tvspainscheduleapi.services;

import com.jtcoding.tvspainscheduleapi.dtos.EventDTO;
import com.jtcoding.tvspainscheduleapi.exceptions.EventException;

public interface EventService {
    EventDTO getEvent(long id) throws EventException;
}
