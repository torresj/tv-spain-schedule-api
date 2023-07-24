package com.jtcoding.tvspainscheduleapi.services;

import com.jtcoding.tvspainscheduleapi.dtos.EventDTO;
import com.jtcoding.tvspainscheduleapi.dtos.PageDTO;
import java.util.List;

public interface SerieService {
  List<EventDTO> getLiveSeries();

  PageDTO getTodaySeries(int nPage, int nElements);

  PageDTO getTomorrowSeries(int nPage, int nElements);
}
