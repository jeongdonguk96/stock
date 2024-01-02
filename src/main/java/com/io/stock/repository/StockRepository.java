package com.io.stock.repository;

import com.io.stock.domain.Stock;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StockRepository extends JpaRepository<Stock, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "SELECT s FROM Stock s WHERE s.id = :id", nativeQuery = true)
    Stock findByIdWithPessimisticLock(@Param("id") Long id);

    @Lock(LockModeType.OPTIMISTIC)
    @Query(value = "SELECT s FROM Stock s WHERE s.id = :id", nativeQuery = true)
    Stock findByIdWithOptimisticLock(@Param("id") Long id);
}
