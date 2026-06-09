package com.balaji.distributor.service;

import com.balaji.distributor.entity.StoreSettings;
import com.balaji.distributor.exceptionHandler.BadRequestException;
import com.balaji.distributor.repository.StoreSettingsRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreSettingsService {

    private final StoreSettingsRepository
            storeSettingsRepo;

    // GET SETTINGS
    @Transactional
    public StoreSettings
    getSettings() {

        return storeSettingsRepo
                .findAll()
                .stream()
                .findFirst()
                .orElseGet(() -> {

                    StoreSettings settings =
                            new StoreSettings();

                    settings.setGstPercentage(5);

                    settings.setShippingCharges(100);

                    return storeSettingsRepo
                            .save(settings);
                });
    }

    // UPDATE SETTINGS
    @Transactional
    public StoreSettings
    updateSettings(
            StoreSettings settings
    ) {
        if (settings == null) {

            throw new BadRequestException(
                    "Settings data is required"
            );
        }

        StoreSettings existing =
                getSettings();

        if (
                settings.getGstPercentage() < 0
                        ||
                        settings.getGstPercentage() > 100
                        ||
                        settings.getShippingCharges() < 0
        ) {

            throw new BadRequestException(
                    "Invalid store settings"
            );
        }

        existing.setGstPercentage(
                settings.getGstPercentage()
        );

        existing.setShippingCharges(
                settings.getShippingCharges()
        );

        return storeSettingsRepo
                .save(existing);
    }
}
