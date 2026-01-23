package com.example.shop.service;

import com.example.shop.entity.ItemImg;
import com.example.shop.repository.ItemImgRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional  //?
public class ItemImgService {

    @Value("${itemImgLocation}")
    private String itemImgLocation;

    //DB저장
    private final ItemImgRepository itemImgRepository;
    //SSD저장
    private final FileService fileService;

    public void saveItemImg(ItemImg itemImg, MultipartFile itmeImgFile)throws Exception{
        String oriImgName = itmeImgFile.getOriginalFilename();
        String imgName = "";
        String imgUrl = "";

        //파일업로드
        if(!StringUtils.isEmpty(oriImgName)){
            imgName  = fileService.uploadFile(itemImgLocation, oriImgName, itmeImgFile.getBytes());
            imgUrl = "/images/item/" + imgName;
        }

        //상품 이미지 정보 저장 - 원본이름 , ssd저장된이름,
        itemImg.updateItemImg(oriImgName, imgName, imgUrl);
        itemImgRepository.save(itemImg);
    }
}
