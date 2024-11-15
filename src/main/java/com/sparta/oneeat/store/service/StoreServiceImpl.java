package com.sparta.oneeat.store.service;

import com.sparta.oneeat.category.entity.Category;
import com.sparta.oneeat.category.repository.StoreCategotyRepository;
import com.sparta.oneeat.category.service.StoreCategoryService;
import com.sparta.oneeat.common.exception.CustomException;
import com.sparta.oneeat.common.exception.ExceptionType;
import com.sparta.oneeat.store.dto.CreateStoreReqDto;
import com.sparta.oneeat.store.dto.CreateStoreResDto;
import com.sparta.oneeat.store.entity.Store;
import com.sparta.oneeat.store.dto.StoreListDto;
import com.sparta.oneeat.store.entity.DeliveryRegion;
import com.sparta.oneeat.store.entity.StoreStatusEnum;
import com.sparta.oneeat.store.repository.DeliveryRegionRepository;
import com.sparta.oneeat.store.repository.StoreRepository;
import com.sparta.oneeat.user.entity.User;
import com.sparta.oneeat.user.entity.UserRoleEnum;
import com.sparta.oneeat.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreServiceImpl implements StoreService{

    private final StoreRepository storeRepository;
    private final DeliveryRegionRepository deliveryRegionRepository;
    private final UserRepository userRepository;
    private final StoreCategotyRepository storeCategotyRepository;
    private final StoreCategoryService storeCategoryService;

    @Override
    public Page<StoreListDto> getStoreList(long userId, int page, int size, String sort, boolean isAsc) {
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort1 = Sort.by(direction, sort);
        Pageable pageable = PageRequest.of(page, size, sort1);

        // 유저 권한 확인
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ExceptionType.INTERNAL_SERVER_ERROR));
        UserRoleEnum userRole = user.getRole();

        // 사용자의 주소를 가져옵니다.
        String userAddress = user.getCurrentAddress();

        List<DeliveryRegion> deliveryRegions = deliveryRegionRepository.findAllByDeliveryRegion(userAddress);
        List<UUID> storeIds = deliveryRegions.stream()
                .map(DeliveryRegion::getStore)
                .map(Store::getId)
                .collect(Collectors.toList());

        Page<Store> storeList = storeRepository.findAllByIdIn(storeIds, pageable);
        
        return storeList.map(StoreListDto::new);
    }

    @Override
    @Transactional
    public CreateStoreResDto createStore(User user, CreateStoreReqDto createStoreReqDto) {
        // 카테고리 조회
        Category category = storeCategotyRepository.findByCategoryName(createStoreReqDto.getCategory())
                .orElseThrow(() -> new CustomException(ExceptionType.CATEGORY_NOT_FOUND));
        // 중복 검사
        if (storeRepository.existsByNameAndCategoryAndAddress(
                createStoreReqDto.getName(),
                category,
                createStoreReqDto.getAddress())) {
            throw new CustomException(ExceptionType.STORE_ALREADY_EXISTS);
        }
        // Store 엔티티 생성
        Store store = new Store(
                user,
                category,
                createStoreReqDto.getName(),
                createStoreReqDto.getAddress(),
                createStoreReqDto.getDescription(),
                StoreStatusEnum.CLOSED,
                createStoreReqDto.getStartTime(),
                createStoreReqDto.getEndTime(),
                createStoreReqDto.getOwner(),
                createStoreReqDto.getMinPrice()
        );

        store = storeRepository.save(store);

        // 배달 지역 처리
        Store finalStore = store;
        List<DeliveryRegion> deliveryRegions = createStoreReqDto.getDeliveryRegions().stream()
                .map(region -> new DeliveryRegion(finalStore, region))
                .collect(Collectors.toList());

        deliveryRegionRepository.saveAll(deliveryRegions);

        // 응답 DTO 생성 및 반환
        List<String> deliveryRegionStrings = deliveryRegions.stream()
                .map(DeliveryRegion::getDeliveryRegion)
                .collect(Collectors.toList());

        return new CreateStoreResDto(store.getId(), deliveryRegionStrings);
    }
}
