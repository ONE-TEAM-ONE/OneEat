package com.sparta.oneeat.menu.repository;

import com.sparta.oneeat.menu.entity.Menu;
import com.sparta.oneeat.store.entity.Store;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends JpaRepository<Menu, UUID> {

}
