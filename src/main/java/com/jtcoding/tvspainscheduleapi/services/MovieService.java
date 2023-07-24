package com.jtcoding.tvspainscheduleapi.services;

import com.jtcoding.tvspainscheduleapi.dtos.EventDTO;
import com.jtcoding.tvspainscheduleapi.dtos.PageDTO;

import java.util.List;

public interface MovieService {
  List<EventDTO> getLiveMovies();

  PageDTO getTodayMovies(int nPage, int nElements);

  PageDTO getTomorrowMovies(int nPage, int nElements);
}
