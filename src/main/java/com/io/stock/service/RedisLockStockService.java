package com.io.stock.service;

import com.io.stock.repository.RedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisLockStockService {

    private final RedisRepository redisRepository;
    private final StockService stockService;

    public void decrease(Long id, Long quantity) throws InterruptedException {
        while (!redisRepository.lock(id)) {
            Thread.sleep(100);
        }

        try {
            stockService.decrease(id, quantity);
        } finally {
            redisRepository.unLock(id);
        }
    }

}
