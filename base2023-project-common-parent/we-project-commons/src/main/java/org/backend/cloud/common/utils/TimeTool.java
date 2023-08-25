package org.backend.cloud.common.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class TimeTool {

  public static Date now() {
    return Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
  }

  public static long nowMilli() {
    return LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
  }
}
