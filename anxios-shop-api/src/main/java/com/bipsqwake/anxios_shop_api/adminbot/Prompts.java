package com.bipsqwake.anxios_shop_api.adminbot;

import java.util.HashMap;
import java.util.Map;

public class Prompts {
    public static Map<AdminCommand, String> requestMessages = new HashMap<>();

    static {
        requestMessages.put(AdminCommand.ITEM_NAME, "Введите новое название");
        requestMessages.put(AdminCommand.ITEM_DESCRIPTION, "Введите новое описание");
        requestMessages.put(AdminCommand.ITEM_PRICE, "Введите новую цену");
        requestMessages.put(AdminCommand.ITEM_OLD_PRICE, "Введите новую старую цену");
        requestMessages.put(AdminCommand.ITEM_STOCK, "Введите новое наличие");
    }

    public static final String CANT_DO_THIS = "Я пока этого не умею";
    public static final String ERROR = "Что-то пошло не так";
    public static final String NUMBER_REQUIRED = "Необходимо ввести число";
    public static final String SUCCESSFULL_UPDATE = "Успешно обновлено";
    public static final String FAILED_UPDATE = "Не получилось обновить";

}
