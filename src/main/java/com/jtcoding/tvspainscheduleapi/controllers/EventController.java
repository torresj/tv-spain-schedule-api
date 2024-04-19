package com.jtcoding.tvspainscheduleapi.controllers;

import com.jtcoding.tvspainscheduleapi.dtos.EventDTO;
import com.jtcoding.tvspainscheduleapi.dtos.PageDTO;
import com.jtcoding.tvspainscheduleapi.exceptions.EventException;
import com.jtcoding.tvspainscheduleapi.services.EventService;
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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/events")
@Slf4j
@RequiredArgsConstructor
public class EventController {

  private final EventService eventService;

  @Operation(summary = "Get event by id")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Success",
            content = {
              @Content(
                  mediaType = "application/json",
                      schema = @Schema(implementation = EventDTO.class))
            }),
              @ApiResponse(responseCode = "404", description = "Not found", content = @Content),
      })
  @GetMapping("/{id}")
  public ResponseEntity<EventDTO> getEvent(@Parameter(description = "Event id") @PathVariable long id) throws EventException {
    log.info("Getting event {}", id);
    var event = eventService.getEvent(id);
    log.info("Event {} found: {}",id, event.getName());
    return ResponseEntity.ok(event);
  }
}
