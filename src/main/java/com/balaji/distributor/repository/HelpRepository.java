package com.balaji.distributor.repository;

import com.balaji.distributor.entity.HelpRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HelpRepository extends JpaRepository<HelpRequest, Long> {

    List<HelpRequest> findAllByOrderByIdDesc();
    List<HelpRequest>
    findByUserIdOrderByIdDesc(
            Long userId
    );
}