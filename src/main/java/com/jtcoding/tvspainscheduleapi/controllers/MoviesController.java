package com.jtcoding.tvspainscheduleapi.controllers;

import com.jtcoding.tvspainscheduleapi.dtos.EventDTO;
import com.jtcoding.tvspainscheduleapi.services.MovieService;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/movies")
@Slf4j
@RequiredArgsConstructor
public class MoviesController {

  private final MovieService movieService;

  @GetMapping("/live")
  public ResponseEntity<List<EventDTO>> getLiveMovies() {
    log.info("Getting live movies events");
    log.info("Time: " + LocalDateTime.now(ZoneId.of("CET")).format(DateTimeFormatter.ofPattern("dd/MM/yy HH:mm")));
    var events = movieService.getLiveMovies();
    log.info("live movies event found: " + events.size());
    return ResponseEntity.ok(events);
  }
}
