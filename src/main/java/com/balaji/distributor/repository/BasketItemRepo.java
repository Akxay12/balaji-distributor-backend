package com.balaji.distributor.repository;

import com.balaji.distributor.entity.BasketItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface BasketItemRepo
        extends JpaRepository<BasketItem, Long> {

    List<BasketItem> findByBasket_Id(Long basketId);

    Optional<BasketItem> findByBasket_IdAndProduct_Id(
            Long basketId,
            Long productId
    );

    @Modifying
    @Transactional
    void deleteByBasket_Id(Long basketId);
}
