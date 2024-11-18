package com.sparta.oneeat.menu.repository;

import com.sparta.oneeat.menu.entity.Menu;
import com.sparta.oneeat.store.entity.Store;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends JpaRepository<Menu, UUID> {

    Page<Menu> findAllByStoreAndDeletedAtIsNull(Store store, Pageable pageable);
    Optional<Menu> findByIdAndStore(UUID menuId, Store store);
    Optional<Menu> findByIdAndStoreIdAndDeletedAtIsNull(UUID menuId, UUID storeId);

}
