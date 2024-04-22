package com.jtcoding.tvspainscheduleapi.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ChannelDTO {
  private long id;
  private String name;
  private String logoUrl;
}
