package com.example.bookverse.repository;

import com.example.bookverse.domain.Order;
import com.example.bookverse.dto.enums.OrderStatus;
import com.example.bookverse.dto.enums.PaymentMethod;
import com.example.bookverse.dto.enums.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Không JOIN FETCH {@code book.authors} trong cùng query: Hibernate cấm fetch đồng thời nhiều “bag”
     * ({@code orderDetails} + {@code authors} đều là {@code List}) → {@code MultipleBagFetchException} → HTTP 500.
     * Tác giả được nạp lazy trong cùng transaction khi map {@link com.example.bookverse.dto.response.ResOrderDTO}.
     */
    @Query("""
            SELECT DISTINCT o FROM Order o
            LEFT JOIN FETCH o.customer
            LEFT JOIN FETCH o.orderDetails od
            LEFT JOIN FETCH od.book b
            LEFT JOIN FETCH b.publisher
            WHERE o.id = :id
            """)
    Optional<Order> fetchDetailById(@Param("id") long id);

    /** Chỉ phân trang id; chi tiết nạp qua {@link #fetchDetailById(long)} để tránh lỗi fetch join + page. */
    Page<Order> findByCustomer_Id(long customerId, Pageable pageable);

    @Query("""
            SELECT DISTINCT o FROM Order o
            LEFT JOIN FETCH o.orderDetails od
            LEFT JOIN FETCH od.book b
            LEFT JOIN FETCH b.publisher
            WHERE o.customer.id = :customerId
            ORDER BY o.createdAt DESC
            """)
    List<Order> fetchDetailsByCustomerId(@Param("customerId") long customerId);

    @Query("""
            SELECT DISTINCT o FROM Order o
            LEFT JOIN FETCH o.orderDetails od
            LEFT JOIN FETCH od.book b
            LEFT JOIN FETCH b.publisher
            ORDER BY o.createdAt DESC
            """)
    List<Order> fetchAllWithDetails();

    List<Order> findByPaymentMethodAndPaymentStatusAndCreatedAtBefore(
            PaymentMethod paymentMethod, PaymentStatus paymentStatus, Instant cutoff);

    long countByCreatedAtBetween(Instant from, Instant to);

    long countByStatusAndCreatedAtBetween(OrderStatus status, Instant from, Instant to);

    @Query("""
            SELECT COALESCE(SUM(o.totalPrice), 0) FROM Order o
            WHERE o.createdAt BETWEEN :from AND :to
            AND o.status <> com.example.bookverse.dto.enums.OrderStatus.CANCELLED
            """)
    Double sumRevenueByCreatedAtBetween(@Param("from") Instant from, @Param("to") Instant to);

    @Query("""
            SELECT o.status, COUNT(o.id) FROM Order o
            WHERE o.createdAt BETWEEN :from AND :to
            GROUP BY o.status
            """)
    List<Object[]> countOrderByStatusBetween(@Param("from") Instant from, @Param("to") Instant to);

    @Query(value = """
            SELECT DATE_FORMAT(CONVERT_TZ(o.created_at, '+00:00', @@session.time_zone), :pattern) AS label,
                   COALESCE(SUM(CASE WHEN o.status <> 'CANCELLED' THEN o.total_price ELSE 0 END), 0) AS revenue,
                   COUNT(o.id) AS orders
            FROM orders o
            WHERE o.created_at BETWEEN :from AND :to
            GROUP BY label
            ORDER BY MIN(o.created_at) ASC
            """, nativeQuery = true)
    List<Object[]> getRevenueSeries(@Param("from") Instant from,
                                    @Param("to") Instant to,
                                    @Param("pattern") String pattern);
}
