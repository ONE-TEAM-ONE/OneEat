package com.sparta.oneeat.menu.dto.response;

import com.sparta.oneeat.menu.entity.Menu;
import com.sparta.oneeat.menu.entity.MenuStatusEnum;
import java.util.UUID;
import lombok.Data;

@Data
public class MenuResponseDto {

    private UUID id;
    private String name;
    private String description;
    private Integer price;
    private String image;
    private MenuStatusEnum status;

    public MenuResponseDto(Menu menu) {
        this.id = menu.getId();
        this.name = menu.getName();
        this.description = menu.getDescription();
        this.price = menu.getPrice();
        this.image = menu.getImage();
        this.status = menu.getStatus();
    }
}
