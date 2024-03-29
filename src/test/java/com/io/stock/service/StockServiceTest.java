package com.io.stock.service;

import com.io.stock.domain.Stock;
import com.io.stock.repository.StockRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class StockServiceTest {

    @Autowired private StockRepository stockRepository;
    @Autowired private StockService stockService;
    @Autowired private PessimisticLockService pessimisticLockService;
    @Autowired private OptimisticLockServiceImpl optimisticLockServiceimpl;

    @BeforeEach
    void before() {
        stockRepository.save(new Stock(1L, 100L));
        System.out.println("beforeEach stared");
    }

    @AfterEach
    void after() {
        stockRepository.deleteAll();
        System.out.println("afterEach stared");
    }

    @Test
    void 재고감소() {
        stockService.decrease(1L, 1L);

        Stock stock = stockRepository.findById(1L).orElseThrow();
        System.out.println("stock = " + stock);
        assertThat(stock.getQuantity()).isEqualTo(99);
    }

    @Test
    void 동시에100개요청() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);
        System.out.println("threadCount = " + threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
//                    stockService.decrease(1L, 1L);
//                    pessimisticLockService.decrease(1L, 1L);
                    optimisticLockServiceimpl.decrease(1L, 1L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Stock stock = stockRepository.findById(1L).orElseThrow();
        System.out.println("stock = " + stock);
        assertThat(stock.getQuantity()).isEqualTo(0);
    }

}