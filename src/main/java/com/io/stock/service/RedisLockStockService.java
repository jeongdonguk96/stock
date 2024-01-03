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
        // 해당 id 값을 키로 데이터를 생성하는데, 신규 생성 시에만 While문을 벗어난다.
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
