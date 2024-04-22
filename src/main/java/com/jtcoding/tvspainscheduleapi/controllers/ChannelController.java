package com.jtcoding.tvspainscheduleapi.controllers;

import com.jtcoding.tvspainscheduleapi.dtos.ChannelDTO;
import com.jtcoding.tvspainscheduleapi.dtos.EventDTO;
import com.jtcoding.tvspainscheduleapi.exceptions.ChannelException;
import com.jtcoding.tvspainscheduleapi.exceptions.EventException;
import com.jtcoding.tvspainscheduleapi.services.ChannelService;
import com.jtcoding.tvspainscheduleapi.services.EventService;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/channels")
@Slf4j
@RequiredArgsConstructor
public class ChannelController {

  private final ChannelService channelService;

  @Operation(summary = "Get channel by id")
  @ApiResponses(
          value = {
                  @ApiResponse(
                          responseCode = "200",
                          description = "Success",
                          content = {
                                  @Content(
                                          mediaType = "application/json",
                                          schema = @Schema(implementation = ChannelDTO.class))
                          }),
                  @ApiResponse(responseCode = "404", description = "Not found", content = @Content),
          })
  @GetMapping("/{id}")
  public ResponseEntity<ChannelDTO> getChannel(@Parameter(description = "Channel id") @PathVariable long id) throws ChannelException {
    log.info("Getting channel {}", id);
    var channel = channelService.getChannel(id);
    log.info("Channel {} found: {}",id, channel.getName());
    return ResponseEntity.ok(channel);
  }

  @Operation(summary = "Get channels")
  @ApiResponses(
          value = {
                  @ApiResponse(
                          responseCode = "200",
                          description = "Success",
                          content = {
                                  @Content(
                                          mediaType = "application/json",
                                          array = @ArraySchema(schema = @Schema(implementation = ChannelDTO.class)))
                          }),
          })
  @GetMapping
  public ResponseEntity<List<ChannelDTO>> getChannels() {
    log.info("Getting channels");
    var channels = channelService.getChannels();
    log.info("Channels found: " + channels.size());
    return ResponseEntity.ok(channels);
  }
}
