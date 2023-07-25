package com.jtcoding.tvspainscheduleapi.controllers;

import com.jtcoding.tvspainscheduleapi.dtos.EventDTO;
import com.jtcoding.tvspainscheduleapi.dtos.PageDTO;
import com.jtcoding.tvspainscheduleapi.services.MovieService;
import com.jtcoding.tvspainscheduleapi.services.SerieService;
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
@RequestMapping("/v1/series")
@Slf4j
@RequiredArgsConstructor
public class SeriesController {

  private static final int MAX_ELEMENTS_PER_PAGE = 50;

  private final SerieService serieService;

  @Operation(summary = "Get live series (CET)")
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
  public ResponseEntity<List<EventDTO>> getLiveSeries() {
    log.info("Getting live series events");
    var events = serieService.getLiveSeries();
    log.info("live series event found: " + events.size());
    return ResponseEntity.ok(events);
  }

  @Operation(summary = "Get today series (CET)")
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
  public ResponseEntity<PageDTO> getTodaySeries(
      @Parameter(description = "Number of page") @RequestParam int nPage,
      @Parameter(description = "Number of elements per page") @RequestParam int nElements) {
    log.info("Getting today series events");
    var page =
        serieService.getTodaySeries(
            nPage, nElements > MAX_ELEMENTS_PER_PAGE ? MAX_ELEMENTS_PER_PAGE : nElements);
    log.info("Today series event found: " + page.getTotalElements());
    return ResponseEntity.ok(page);
  }

  @Operation(summary = "Get tomorrow series (CET)")
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
  public ResponseEntity<PageDTO> getTomorrowSeries(
      @Parameter(description = "Number of page") @RequestParam int nPage,
      @Parameter(description = "Number of elements per page") @RequestParam int nElements) {
    log.info("Getting tomorrow series events");
    var page =
        serieService.getTomorrowSeries(
            nPage, nElements > MAX_ELEMENTS_PER_PAGE ? MAX_ELEMENTS_PER_PAGE : nElements);
    log.info("Tomorrow series event found: " + page.getTotalElements());
    return ResponseEntity.ok(page);
  }
}
