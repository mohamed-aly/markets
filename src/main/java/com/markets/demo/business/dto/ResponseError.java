package com.markets.demo.business.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class ResponseError {

  private final String title;
  private final LocalDateTime timeStamp;
  private final String message;

}
