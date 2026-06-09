package com.balaji.distributor.repository;

import com.balaji.distributor.entity.Basket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BasketRepository extends JpaRepository<Basket, Long> {

    Optional<Basket> findByIdAndUserId(
            Long basketId,
            Long userId
    );

    List<Basket> findByUserId(Long userId);

    Optional<Basket> findByUserIdAndStatus(
            Long userId,
            String status
    );

    Optional<Basket> findTopByUserIdOrderByBasketNumberDesc(
            Long userId
    );

}
