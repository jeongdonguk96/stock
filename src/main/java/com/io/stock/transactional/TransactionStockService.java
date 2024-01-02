package com.io.stock.transactional;

import com.io.stock.service.StockService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TransactionStockService {

    private final StockService stockService;

    public void decrease(Long id, Long quantity) {
        startTransaction();

        stockService.decrease(id, quantity);

        endTransaction();
    }

    private void startTransaction() {
        System.out.println("transaction started");
    }

    private void endTransaction() {
        System.out.println("commit;");
    }
}
