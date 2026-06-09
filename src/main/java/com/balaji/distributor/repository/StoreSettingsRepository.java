package com.balaji.distributor.repository;


import com.balaji.distributor.entity.StoreSettings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreSettingsRepository
        extends JpaRepository<StoreSettings, Long> {
}
