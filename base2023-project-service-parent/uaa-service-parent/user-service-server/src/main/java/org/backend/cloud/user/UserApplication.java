package org.backend.cloud.user;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan({"org.backend.cloud.user.mapper"})
@EnableTransactionManagement
@EnableDiscoveryClient
@EnableCaching // 使用SpringCache
public class UserApplication {

  public static void main(String[] args) {
    SpringApplication.run(UserApplication.class, args);
  }

  /**
   * 使用Redis作为缓存数据库
   */
  @Bean
  public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
    RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
        .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(
            new GenericJackson2JsonRedisSerializer()));
    // 使用一个冒号进行拼接RedisKey前缀。默认是使用两个冒号，太难看了！
    config = config.computePrefixWith(cacheName -> cacheName + ":");
    return RedisCacheManager.builder(redisConnectionFactory).cacheDefaults(config).build();
  }

  /**
   * 创建用户时，需要对用户密码进行加密，用到了PasswordEncoder。
   * 注意：创建用户时的用户名密码加密算法需要和登陆时校验用户密码的算法一致
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
