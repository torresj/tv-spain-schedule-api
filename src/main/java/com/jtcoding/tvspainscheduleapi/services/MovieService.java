package com.jtcoding.tvspainscheduleapi.services;

import com.jtcoding.tvspainscheduleapi.dtos.EventDTO;
import java.util.List;

public interface MovieService {
  List<EventDTO> getLiveMovies();

  List<EventDTO> getTodayMovies();

  List<EventDTO> getTomorrowMovies();
}
