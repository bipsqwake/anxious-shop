package com.bipsqwake.anxios_shop_api.adminbot;

import java.util.HashMap;
import java.util.Map;

import com.bipsqwake.anxios_shop_api.adminbot.fsm.context.ItemCreateContext;

public class Constants {
    public static Map<AdminCommand, String> requestMessages = new HashMap<>();
    public static Map<ItemCreateContext.CreateSubState, String> createMessages = new HashMap<>();

    static {
        requestMessages.put(AdminCommand.ITEM_NAME, "Введите новое название");
        requestMessages.put(AdminCommand.ITEM_DESCRIPTION, "Введите новое описание");
        requestMessages.put(AdminCommand.ITEM_PRICE, "Введите новую цену");
        requestMessages.put(AdminCommand.ITEM_OLD_PRICE, "Введите новую старую цену");
        requestMessages.put(AdminCommand.ITEM_STOCK, "Введите новое наличие");

        createMessages.put(ItemCreateContext.CreateSubState.ID, "Введите ID");
        createMessages.put(ItemCreateContext.CreateSubState.NAME, "Введите название");
        createMessages.put(ItemCreateContext.CreateSubState.DESCRIPTION, "Введите описание");
        createMessages.put(ItemCreateContext.CreateSubState.PRICE, "Введите цену");
        createMessages.put(ItemCreateContext.CreateSubState.OLD_PRICE, "Введите старую цену (0 при отсутствии)");
        createMessages.put(ItemCreateContext.CreateSubState.STOCK, "Введите наличие");
        createMessages.put(ItemCreateContext.CreateSubState.IMG, "Выберите изображение");
        createMessages.put(ItemCreateContext.CreateSubState.IMG_SIZE, "Выберите размер изображения");
    }

    //Requests
    public static final String CONFIRM_ITEM_REMOVE = "Вы уверены, что хотите удалить товар %s %s";

    //Feedback
    public static final String CANT_DO_THIS = "Я пока этого не умею";
    public static final String ERROR = "Что-то пошло не так";
    public static final String NUMBER_REQUIRED = "Необходимо ввести число";
    public static final String SUCCESSFULL_UPDATE = "Успешно обновлено";
    public static final String FAILED_UPDATE = "Не получилось обновить";
    public static final String SUCCESSFULL_DELETE = "Успешно удалено";
    public static final String ID_PATTERN = "ID может содержать только латинские буквы, цифры и '-' '_'";

    //internal
    public static final String JPG_FORMAT = ".jpg";

}
