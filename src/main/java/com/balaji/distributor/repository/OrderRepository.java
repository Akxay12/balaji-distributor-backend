package com.balaji.distributor.repository;

import com.balaji.distributor.entity.Order;
import com.balaji.distributor.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository
        extends JpaRepository<Order, Long> {

    List<Order>
    findByUserOrderByCreatedAtDesc(
            User user
    );

    List<Order>
    findAllByOrderByCreatedAtDesc();

    long countByUserId(Long userId);

    @Query("""
SELECT COALESCE(
       SUM(o.totalAmount),
       0
)
FROM Order o
WHERE o.user.id = :userId
""")
    Double getLifetimePurchase(
            @Param("userId")
            Long userId
    );

}