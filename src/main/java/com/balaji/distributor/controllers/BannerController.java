package com.balaji.distributor.controllers;

import com.balaji.distributor.entity.Banner;
import com.balaji.distributor.service.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/banner")
@CrossOrigin("*")
public class BannerController {

    @Autowired
    private BannerService bannerService;

    // SAVE BANNER
    @PostMapping("/admin/save")
    public ResponseEntity<Banner> saveBanner(
            @RequestBody Banner banner
    ) {

        return ResponseEntity.ok(
                bannerService.saveBanner(banner)
        );
    }

    // GET BANNER
    @GetMapping("/get")
    public ResponseEntity<Banner> getBanner() {

        return ResponseEntity.ok(
                bannerService.getBanner()
        );
    }
}
