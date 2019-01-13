package com.rsone.binservice.rest;

import com.rsone.binservice.model.Bin;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class BinController {

    private final ReactiveRedisOperations<String, Bin> redisOps;

    public BinController(ReactiveRedisOperations<String, Bin> redisOps) {
        this.redisOps = redisOps;
    }

    @GetMapping("/bins")
    public Flux<Bin> getAllBins(){
        return redisOps.keys("*")
                .flatMap(redisOps.opsForValue()::get);
    }
}
