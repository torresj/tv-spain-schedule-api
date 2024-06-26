package com.jtcoding.tvspainscheduleapi.dtos;

import com.jtcoding.tvspainscheduleapi.enums.EventType;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class EventDTO {
  private long id;
  private EventType eventType;
  private LocalDateTime start;
  private LocalDateTime end;
  private long duration;
  private int progress;
  private String name;
  private String synopsis;
  private String classification;
  private String director;
  private String interpreters;
  private Double rate;
  private String imageUrl;
  private String chapterName;
  private ChannelDTO channel;
}
