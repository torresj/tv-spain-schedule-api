package com.jtcoding.tvspainscheduleapi.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class PageDTO {
  private long totalElements;
  private int totalPages;
  private int elements;
  private int page;
  List<EventDTO> events;
}
