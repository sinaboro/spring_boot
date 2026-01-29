package com.example.shop.service;

import com.example.shop.dto.ItemFormDto;
import com.example.shop.dto.ItemImgDto;
import com.example.shop.dto.ItemSearchDto;
import com.example.shop.dto.MainItemDto;
import com.example.shop.entity.Item;
import com.example.shop.entity.ItemImg;
import com.example.shop.repository.ItemImgRepository;
import com.example.shop.repository.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemImgService itemImgService;
    private final ItemImgRepository itemImgRepository;

    public Long savedItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws  Exception{

        //상품등록 - : dto->entity
        Item item = itemFormDto.createItem();
        itemRepository.save(item);

        //이미지 등록
        for(int i=0; i<itemImgFileList.size(); i++){
            ItemImg itemImg = new ItemImg();
            itemImg.setItem(item);

            if(i == 0){
                itemImg.setRepimgYn("Y");
            }else{
                itemImg.setRepimgYn("N");
            }
            itemImgService.saveItemImg(itemImg, itemImgFileList.get(i));
        }

        return item.getId();

    }

    @Transactional(readOnly = true)
    public ItemFormDto getItemDtl(Long itemId){

        List<ItemImg> itemImgList = itemImgRepository.findItemImgByItemIdOrderByIdAsc(itemId);

        // ItemImg있는 것을 화면상 보여주기 위해서 ItemImgDto변환
        List<ItemImgDto> itemImgDtoList = new ArrayList<>();

        for(ItemImg itemImg: itemImgList){
            ItemImgDto itemImgDto = ItemImgDto.of(itemImg);
            itemImgDtoList.add(itemImgDto);
        }

        //저장된 상품정보 가져오기
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException());


        //itemImgDtoList + item  ---> itemFormDto
        ItemFormDto itemFormDto = ItemFormDto.of(item);
        itemFormDto.setItemImgDtoList(itemImgDtoList);

        log.info("itemFormDto : " + itemFormDto);

        return itemFormDto;
    }

    public Long updateItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception{

        //상품 조회  - Item(수정전)
        Item item = itemRepository.findById(itemFormDto.getId())
                .orElseThrow(EntityNotFoundException::new);

        // 상품 수정  - Item(수정후)
        item.updateItem(itemFormDto);

        log.info("-------------item update-----------------------");

        //이미지 수정 파트
        List<Long> itemImgIds = itemFormDto.getItemImgIds();

        for(int i=0; i<itemImgFileList.size(); i++){
            itemImgService.updateItemImg(itemImgIds.get(i), itemImgFileList.get(i));
        }

        return item.getId();
    }

    @Transactional(readOnly = true)
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable){

        return itemRepository.getAdminItemPage(itemSearchDto, pageable);
    }

    @Transactional(readOnly = true)
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable){

        return itemRepository.getMainItemPage(itemSearchDto, pageable);
    }

}
