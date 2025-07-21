package com.bipsqwake.anxios_shop_api.adminbot.fsm.context;

import java.util.List;

import org.telegram.telegrambots.meta.api.objects.photo.PhotoSize;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemCreateContext {
    private String intId;
    private String name;
    private String description;
    private int price;
    private int oldPrice;
    private int stock;
    private List<PhotoSize> photos;
    private PhotoSize selectedPhoto;
    private String selectedSize;
    private CreateSubState subState;
    private String imgUrl;

    public static enum CreateSubState {
        ID,
        NAME,
        DESCRIPTION,
        PRICE,
        OLD_PRICE,
        STOCK,
        IMG,
        IMG_SIZE
    }
}
