package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.api.dto.Ad;
import ru.skypro.homework.api.dto.Ads;
import ru.skypro.homework.api.dto.CreateOrUpdateAd;
import ru.skypro.homework.api.dto.ExtendedAd;

public interface AdService {

    Ads getAllAds();

    Ad addAd(String username, CreateOrUpdateAd properties, MultipartFile image);

    Ads getCurrentUserAds(String username);

    ExtendedAd getAd(Integer id);
}
