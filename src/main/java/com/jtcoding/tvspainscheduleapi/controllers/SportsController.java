package com.jtcoding.tvspainscheduleapi.controllers;

import com.jtcoding.tvspainscheduleapi.dtos.EventDTO;
import com.jtcoding.tvspainscheduleapi.dtos.PageDTO;
import com.jtcoding.tvspainscheduleapi.services.SportsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/sports")
@Slf4j
@RequiredArgsConstructor
public class SportsController {

  private static final int MAX_ELEMENTS_PER_PAGE = 50;

  private final SportsService sportsService;

  @Operation(summary = "Get live sports (CET)")
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
  public ResponseEntity<List<EventDTO>> getLiveSports() {
    log.info("Getting live sports events");
    var events = sportsService.getLiveSports();
    log.info("live sports event found: " + events.size());
    return ResponseEntity.ok(events);
  }

  @Operation(summary = "Get today sports (CET)")
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
  public ResponseEntity<PageDTO> getTodaySports(
      @Parameter(description = "Number of page") @RequestParam int nPage,
      @Parameter(description = "Number of elements per page") @RequestParam int nElements) {
    log.info("Getting today sports events");
    var page =
        sportsService.getTodaySports(
            nPage, nElements > MAX_ELEMENTS_PER_PAGE ? MAX_ELEMENTS_PER_PAGE : nElements);
    log.info("Today sports event found: " + page.getTotalElements());
    return ResponseEntity.ok(page);
  }

  @Operation(summary = "Get tomorrow sports (CET)")
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
  public ResponseEntity<PageDTO> getTomorrowSports(
      @Parameter(description = "Number of page") @RequestParam int nPage,
      @Parameter(description = "Number of elements per page") @RequestParam int nElements) {
    log.info("Getting tomorrow sports events");
    var page =
        sportsService.getTomorrowSports(
            nPage, nElements > MAX_ELEMENTS_PER_PAGE ? MAX_ELEMENTS_PER_PAGE : nElements);
    log.info("Tomorrow sports event found: " + page.getTotalElements());
    return ResponseEntity.ok(page);
  }
}
