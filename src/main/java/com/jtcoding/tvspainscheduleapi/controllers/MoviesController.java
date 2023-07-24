package com.jtcoding.tvspainscheduleapi.controllers;

import com.jtcoding.tvspainscheduleapi.dtos.EventDTO;
import com.jtcoding.tvspainscheduleapi.dtos.PageDTO;
import com.jtcoding.tvspainscheduleapi.services.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/movies")
@Slf4j
@RequiredArgsConstructor
public class MoviesController {

  private static final int MAX_ELEMENTS_PER_PAGE = 50;

  private final MovieService movieService;

  @Operation(summary = "Get live movies (CET)")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Success",
            content = {
              @Content(
                  mediaType = "application/json",
                  array = @ArraySchema(schema = @Schema(implementation = EventDTO.class)))
            }),
      })
  @GetMapping("/live")
  public ResponseEntity<List<EventDTO>> getLiveMovies() {
    log.info("Getting live movies events");
    var events = movieService.getLiveMovies();
    log.info("live movies event found: " + events.size());
    return ResponseEntity.ok(events);
  }

  @Operation(summary = "Get today movies (CET)")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Success",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = PageDTO.class))
            })
      })
  @GetMapping("/today")
  public ResponseEntity<PageDTO> getTodayMovies(
      @Parameter(description = "Number of page") @RequestParam int nPage,
      @Parameter(description = "Number of elements per page") @RequestParam int nElements) {
    log.info("Getting today movies events");
    var page =
        movieService.getTodayMovies(
            nPage, nElements > MAX_ELEMENTS_PER_PAGE ? MAX_ELEMENTS_PER_PAGE : nElements);
    log.info("Today movies event found: " + page.getTotalElements());
    return ResponseEntity.ok(page);
  }

  @Operation(summary = "Get tomorrow movies (CET)")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Success",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = PageDTO.class))
            })
      })
  @GetMapping("/tomorrow")
  public ResponseEntity<PageDTO> getTomorrowMovies(
      @Parameter(description = "Number of page") @RequestParam int nPage,
      @Parameter(description = "Number of elements per page") @RequestParam int nElements) {
    log.info("Getting tomorrow movies events");
    var page =
        movieService.getTomorrowMovies(
            nPage, nElements > MAX_ELEMENTS_PER_PAGE ? MAX_ELEMENTS_PER_PAGE : nElements);
    log.info("Tomorrow movies event found: " + page.getTotalElements());
    return ResponseEntity.ok(page);
  }
}
