package com.io.stock.service;

import com.io.stock.domain.Stock;
import com.io.stock.repository.StockRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OptimisticLockService {

    private final StockRepository stockRepository;

    @Transactional
    public void decrease(Long id, Long quantity) {
        Stock foundStock = stockRepository.findByIdWithOptimisticLock(id);
        foundStock.decrease(quantity);
        stockRepository.save(foundStock);
    }

}
