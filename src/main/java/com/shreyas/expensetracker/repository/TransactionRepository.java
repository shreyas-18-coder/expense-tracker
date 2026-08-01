package com.shreyas.expensetracker.repository;

import com.shreyas.expensetracker.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

import java.time.LocalDate;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT t FROM Transaction t WHERE t.user.id = :userId " +
            "AND (:categoryId IS NULL OR t.category.id = :categoryId) " +
            "AND (:startDate IS NULL OR t.date >= :startDate) " +
            "AND (:endDate IS NULL OR t.date <= :endDate)")
    Page<Transaction> findFiltered(
            @Param("userId") Long userId,
            @Param("categoryId") Long categoryId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable
    );
    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t " +
            "WHERE t.user.id = :userId AND t.category.id = :categoryId " +
            "AND t.type = 'EXPENSE' " +
            "AND FUNCTION('MONTH', t.date) = :month AND FUNCTION('YEAR', t.date) = :year")
    Double sumExpensesByCategoryAndMonth(
            @Param("userId") Long userId,
            @Param("categoryId") Long categoryId,
            @Param("month") Integer month,
            @Param("year") Integer year
    );
    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t " +
            "WHERE t.user.id = :userId AND t.type = :type " +
            "AND FUNCTION('MONTH', t.date) = :month AND FUNCTION('YEAR', t.date) = :year")
    Double sumByTypeAndMonth(
            @Param("userId") Long userId,
            @Param("type") String type,
            @Param("month") Integer month,
            @Param("year") Integer year
    );

    @Query("SELECT t.category.name, COALESCE(SUM(t.amount), 0) FROM Transaction t " +
            "WHERE t.user.id = :userId AND t.type = 'EXPENSE' " +
            "AND FUNCTION('MONTH', t.date) = :month AND FUNCTION('YEAR', t.date) = :year " +
            "GROUP BY t.category.name")
    List<Object[]> getCategoryBreakdown(
            @Param("userId") Long userId,
            @Param("month") Integer month,
            @Param("year") Integer year
    );

    @Query("SELECT FUNCTION('MONTH', t.date), FUNCTION('YEAR', t.date), COALESCE(SUM(t.amount), 0) " +
            "FROM Transaction t WHERE t.user.id = :userId AND t.type = 'EXPENSE' " +
            "GROUP BY FUNCTION('YEAR', t.date), FUNCTION('MONTH', t.date) " +
            "ORDER BY FUNCTION('YEAR', t.date), FUNCTION('MONTH', t.date)")
    List<Object[]> getMonthlyTrend(@Param("userId") Long userId);
}