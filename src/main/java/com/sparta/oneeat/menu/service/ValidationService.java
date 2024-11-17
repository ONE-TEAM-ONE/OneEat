package com.sparta.oneeat.menu.service;

import com.sparta.oneeat.common.exception.CustomException;
import com.sparta.oneeat.common.exception.ExceptionType;
import com.sparta.oneeat.menu.entity.Menu;
import com.sparta.oneeat.menu.repository.MenuRepository;
import com.sparta.oneeat.store.entity.Store;
import com.sparta.oneeat.store.repository.StoreRepository;
import com.sparta.oneeat.user.entity.User;
import com.sparta.oneeat.user.entity.UserRoleEnum;
import com.sparta.oneeat.user.repository.UserRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidationService {

    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;

    public User validateUser(long userId) {
        // 유저 검증
        return userRepository.findById(userId)
            .orElseThrow(() -> new CustomException(ExceptionType.USER_NOT_EXIST));
    }

    public Store validateStore(User user, UUID storeId) {
        // 가게 검증 / 사장이라면 해당 유저에게 해당 가게가 있는지
        if (user.getRole() == UserRoleEnum.OWNER) {
            return storeRepository.findByIdAndUser(storeId, user)
                .orElseThrow(
                    () -> new CustomException(
                        ExceptionType.INTERNAL_SERVER_ERROR)); //TODO 가게 ExceptionType으로 수정 필요
        } else {
            return storeRepository.findById(storeId).orElseThrow(
                () -> new CustomException(ExceptionType.INTERNAL_SERVER_ERROR)); //TODO 가게 ExceptionType으로 수정 필요
        }
    }

    public Menu validateMenu(UUID menuId) {
        return  menuRepository.findById(menuId)
            .orElseThrow(() -> new CustomException(ExceptionType.MENU_INVALID_REQUEST));
    }

}
