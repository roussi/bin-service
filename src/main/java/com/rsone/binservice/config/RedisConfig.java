package com.rsone.binservice.config;

import com.rsone.binservice.model.Bin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    ReactiveRedisOperations<String, Bin> redisOperations(ReactiveRedisConnectionFactory factory){
        Jackson2JsonRedisSerializer<Bin> serializer= new Jackson2JsonRedisSerializer<>(Bin.class);

        RedisSerializationContext.RedisSerializationContextBuilder<String, Bin> builder =
                RedisSerializationContext.newSerializationContext(new StringRedisSerializer());
        RedisSerializationContext<String, Bin> context = builder.value(serializer).build();
        return new ReactiveRedisTemplate<>(factory, context);
    }
}
