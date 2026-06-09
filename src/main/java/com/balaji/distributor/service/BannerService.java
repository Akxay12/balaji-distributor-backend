package com.balaji.distributor.service;



import com.balaji.distributor.entity.Banner;
import com.balaji.distributor.repository.BannerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BannerService {

    @Autowired
    private BannerRepo bannerRepo;

    // SAVE / UPDATE
    public Banner saveBanner(Banner banner) {

        banner.setId(1L);

        return bannerRepo.save(banner);
    }

    // GET BANNER
    public Banner getBanner() {

        return bannerRepo.findById(1L)
                .orElse(new Banner());
    }
}
