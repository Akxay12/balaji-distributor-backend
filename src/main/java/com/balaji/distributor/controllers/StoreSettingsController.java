package com.balaji.distributor.controllers;

import com.balaji.distributor.entity.StoreSettings;
import com.balaji.distributor.service.StoreSettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/store-settings")
@CrossOrigin("*")
@RequiredArgsConstructor
public class StoreSettingsController {

    private final StoreSettingsService
            storeSettingsService;

    // GET SETTINGS
    @GetMapping
    public StoreSettings
    getSettings() {

        return storeSettingsService
                .getSettings();
    }

    // UPDATE SETTINGS
    @PutMapping
    public StoreSettings
    updateSettings(
            @RequestBody
            StoreSettings settings
    ) {

        return storeSettingsService
                .updateSettings(settings);
    }
}
