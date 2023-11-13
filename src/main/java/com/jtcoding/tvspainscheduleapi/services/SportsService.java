package com.jtcoding.tvspainscheduleapi.services;

import com.jtcoding.tvspainscheduleapi.dtos.EventDTO;
import com.jtcoding.tvspainscheduleapi.dtos.PageDTO;

import java.util.List;

public interface SportsService {
  List<EventDTO> getLiveSports();

  PageDTO getTodaySports(int nPage, int nElements);

  PageDTO getTomorrowSports(int nPage, int nElements);
}
