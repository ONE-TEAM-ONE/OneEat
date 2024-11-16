package com.sparta.oneeat.store.service;

import com.sparta.oneeat.category.entity.Category;
import com.sparta.oneeat.category.repository.StoreCategotyRepository;
import com.sparta.oneeat.common.exception.CustomException;
import com.sparta.oneeat.common.exception.ExceptionType;
import com.sparta.oneeat.menu.dto.response.MenuResponseDto;
import com.sparta.oneeat.menu.entity.Menu;
import com.sparta.oneeat.menu.repository.MenuRepository;
import com.sparta.oneeat.review.dto.ReviewListDto;
import com.sparta.oneeat.review.entity.Review;
import com.sparta.oneeat.review.repository.ReviewRepository;
import com.sparta.oneeat.store.dto.CreateStoreReqDto;
import com.sparta.oneeat.store.dto.CreateStoreResDto;
import com.sparta.oneeat.store.dto.StoreDetailDto;
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
    private final MenuRepository menuRepository;
    private final ReviewRepository reviewRepository;

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

        return new CreateStoreResDto(store.getId());
    }

    @Override
    public StoreDetailDto getStoreDetail(UUID storeId,
                                         Integer menuPage, Integer menuSize, String menuSort, Boolean menuIsAsc,
                                         Integer reviewPage, Integer reviewSize, String reviewSort, Boolean reviewIsAsc) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(ExceptionType.STORE_NOT_EXIST));

        List<String> deliveryRegions = deliveryRegionRepository.findAllByStoreId(storeId).stream()
                .map(DeliveryRegion::getDeliveryRegion)
                .collect(Collectors.toList());

        // 메뉴 페이징 및 정렬 처리
        menuPage = (menuPage == null) ? 0 : menuPage;
        menuSize = (menuSize == null) ? 10 : menuSize;
        menuSort = (menuSort == null) ? "createdAt" : menuSort;
        menuIsAsc = (menuIsAsc == null) ? false : menuIsAsc;

        Sort menuSortObj = Sort.by(menuIsAsc ? Sort.Direction.ASC : Sort.Direction.DESC, menuSort);
        PageRequest menuPageRequest = PageRequest.of(menuPage, menuSize, menuSortObj);

        Page<Menu> menus = menuRepository.findAllByStore(store, menuPageRequest);
        List<MenuResponseDto> menuDtos = menus.getContent().stream()
                .map(MenuResponseDto::new)
                .collect(Collectors.toList());

        // 리뷰 페이징 및 정렬 처리(파라미터 값이 없으면 생성일 기준 최신순으로 10개씩 정렬된 페이지의 첫번째 페이지를 반환한다)
        reviewPage = (reviewPage == null) ? 0 : reviewPage;
        reviewSize = (reviewSize == null) ? 10 : reviewSize;
        reviewSort = (reviewSort == null) ? "createdAt" : reviewSort;
        reviewIsAsc = (reviewIsAsc == null) ? false : reviewIsAsc;

        Sort reviewSortObj = Sort.by(reviewIsAsc ? Sort.Direction.ASC : Sort.Direction.DESC, reviewSort);
        PageRequest reviewPageRequest = PageRequest.of(reviewPage, reviewSize, reviewSortObj);

        Page<Review> reviews = reviewRepository.findAllByStoreAndDeletedAtIsNull(store, reviewPageRequest);
        List<ReviewListDto> reviewDtos = reviews.getContent().stream()
                .map(ReviewListDto::new)
                .collect(Collectors.toList());

        return new StoreDetailDto(store, deliveryRegions, menuDtos, reviewDtos);
    }

}
