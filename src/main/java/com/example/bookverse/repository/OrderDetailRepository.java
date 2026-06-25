package com.example.bookverse.repository;

import com.example.bookverse.domain.OrderDetail;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    @Query("""
            SELECT COALESCE(SUM(od.quantity), 0) FROM OrderDetail od
            WHERE od.order.createdAt BETWEEN :from AND :to
            AND od.order.status <> com.example.bookverse.dto.enums.OrderStatus.CANCELLED
            """)
    Long sumSoldQuantityBetween(Instant from, Instant to);

    @Query(value = """
            SELECT od.book_id AS productId,
                   b.title AS title,
                   COALESCE(SUM(od.quantity), 0) AS soldQty,
                   COALESCE(SUM(od.price * od.quantity), 0) AS revenue
            FROM order_details od
            JOIN orders o ON o.id = od.order_id
            JOIN books b ON b.id = od.book_id
            WHERE o.created_at BETWEEN :from AND :to
              AND o.status <> 'CANCELLED'
            GROUP BY od.book_id, b.title
            ORDER BY soldQty DESC, revenue DESC
            """, nativeQuery = true)
    List<Object[]> findTopProducts(Instant from, Instant to, Pageable pageable);
}
