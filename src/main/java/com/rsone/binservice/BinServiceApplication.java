package com.rsone.binservice;

import com.rsone.binservice.model.Bin;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Log4j2
@SpringBootApplication
public class BinServiceApplication {

	private final ReactiveRedisConnectionFactory connectionFactory;
	private final ReactiveRedisOperations<String, Bin> redisOps;

	public BinServiceApplication(ReactiveRedisConnectionFactory connectionFactory, ReactiveRedisOperations<String, Bin> redisOps) {
		this.connectionFactory = connectionFactory;
		this.redisOps = redisOps;
	}

	public static void main(String[] args) {
		SpringApplication.run(BinServiceApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void runner(){
		log.info("----- Runner -----");

        Flux<Bin> bins = Flux.just("1234", "4444", "5555", "4445", "4446")
                .map(data -> new Bin("bin".concat(data), data));
		connectionFactory.getReactiveConnection()
				.serverCommands().flushAll()
				.thenMany(bins)
				.flatMap(bin-> redisOps.opsForValue().set(bin.getName(), bin))
                .thenMany(redisOps.keys("*").flatMap(redisOps.opsForValue()::get))
                .subscribe(System.out::println);
	}

}

