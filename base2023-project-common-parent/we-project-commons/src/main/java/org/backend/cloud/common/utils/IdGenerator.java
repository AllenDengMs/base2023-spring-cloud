package org.backend.cloud.common.utils;

public class IdGenerator {

  private static final long defaultMachineId = 1;
  private static final long defaultDatacenterId = 1;

  private static SnowflakeUtil snowflake = new SnowflakeUtil(defaultMachineId, defaultDatacenterId);
  /**
   * 在分布式环境，同的微服务，如果使用相同的 machineId和datacenterId，可能会出现重复的id
   */
  public static void register(long machineId, long datacenterId) {
    snowflake = new SnowflakeUtil(machineId, datacenterId);
  }

  /**
   * 全局唯一id
   */
  public static Long nextId() {
    return snowflake.getNextId();
  }
}
