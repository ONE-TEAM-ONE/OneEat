package com.sparta.oneeat.menu.repository;

import com.sparta.oneeat.menu.entity.Ai;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AiRepository extends JpaRepository <Ai, UUID> {

}
