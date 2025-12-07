package com.crni99.bookstore.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableCaching
public class RedisConfig {

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {

        // 1. Tạo & Cấu hình ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();

        // Quan trọng: Đăng ký Module để xử lý LocalDate, LocalDateTime
        objectMapper.registerModule(new JavaTimeModule());

        // (Tùy chọn) Để ngày tháng lưu dạng chuỗi "2025-12-07" dễ đọc thay vì mảng số [2025,12,7]
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // Quan trọng: Để Redis biết được class nào đang được lưu (để deserialize ngược lại)
        objectMapper.activateDefaultTyping(
                LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.PROPERTY
        );

        // 2. Tạo Serializer dùng ObjectMapper đã cấu hình ở trên
        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(objectMapper);

        // 3. Cấu hình Redis Cache
        RedisCacheConfiguration defaults = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10))
                .disableCachingNullValues()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer)); // <--- Dùng serializer mới này

        // 4. Cấu hình riêng cho từng cache (như cũ)
        Map<String, RedisCacheConfiguration> specificConfigs = new HashMap<>();
        specificConfigs.put("categories", defaults.entryTtl(Duration.ofDays(1)));
        specificConfigs.put("books", defaults.entryTtl(Duration.ofHours(1)));
        specificConfigs.put("books_search", defaults.entryTtl(Duration.ofMinutes(30)));

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaults)
                .withInitialCacheConfigurations(specificConfigs)
                .build();
    }
}