package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import ru.skypro.homework.api.dto.Ad;
import ru.skypro.homework.api.dto.Ads;
import ru.skypro.homework.api.dto.CreateOrUpdateAd;
import ru.skypro.homework.api.dto.ExtendedAd;
import ru.skypro.homework.db.model.AdModel;
import ru.skypro.homework.db.model.UserModel;
import ru.skypro.homework.db.repository.AdRepository;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.service.UserService;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class AdServiceImpl implements AdService {

    private static final String AD_IMAGE_PATH = "/ads/%d/image";

    private final AdRepository adRepository;
    private final AdMapper adMapper;
    private final UserService userService;

    @Override
    @Transactional(readOnly = true)
    public Ads getAllAds() {
        return adMapper.toAds(adRepository.findAll());
    }

    @Override
    @Transactional
    public Ad addAd(String username, CreateOrUpdateAd properties, MultipartFile image) {
        UserModel author = userService.getCurrentUserModel(username);
        AdModel ad = adMapper.toModel(properties);
        ad.setAuthor(author);
        ad.setImage(String.format(AD_IMAGE_PATH, 0));
        ad = adRepository.save(ad);
        ad.setImage(String.format(AD_IMAGE_PATH, ad.getId()));
        return adMapper.toDto(adRepository.save(ad));
    }

    @Override
    @Transactional(readOnly = true)
    public Ads getCurrentUserAds(String username) {
        UserModel author = userService.getCurrentUserModel(username);
        return adMapper.toAds(adRepository.findAllByAuthor(author));
    }

    @Override
    @Transactional(readOnly = true)
    public ExtendedAd getAd(Integer id) {
        return adRepository.findById(id)
                .map(adMapper::toExtendedDto)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND));
    }
}
