package com.balaji.distributor.repository;


import com.balaji.distributor.entity.Banner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BannerRepo extends JpaRepository<Banner, Long> {
}
