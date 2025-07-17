package com.bipsqwake.anxios_shop_api.adminbot;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class AdminBotKeyboards {

    public static InlineKeyboardMarkup getRootKeyboard() {
        return genericKeycboard(
                Arrays.asList(
                        Arrays.asList(
                                new ButtonRepresentation("Информация о товаре", AdminCommand.ITEM_INFO.name()),
                                new ButtonRepresentation("Продано", AdminCommand.SOLD_INFO.name()))));
                        // Arrays.asList(
                        //         new ButtonRepresentation("Очистить базу", AdminCommand.CLEAR_DB.name()),
                        //         new ButtonRepresentation("Продано", AdminCommand.SOLD_INFO.name()))));
    }

    public static InlineKeyboardMarkup getBackKeyboard() {
        return genericKeycboard(
                Arrays.asList(
                        Arrays.asList(
                                new ButtonRepresentation("Назад", AdminCommand.BACK.name()))));
    }

    public static InlineKeyboardMarkup getConfirmKeyboard() {
        return genericKeycboard(
                Arrays.asList(
                        Arrays.asList(
                                new ButtonRepresentation("Да", AdminCommand.YES.name()),
                                new ButtonRepresentation("Нет", AdminCommand.NO.name()))));
    }

    public static InlineKeyboardMarkup getItemKeyboard() {
        return genericKeycboard(
                Arrays.asList(
                        Arrays.asList(
                                new ButtonRepresentation("Название", AdminCommand.ITEM_NAME.name()),
                                new ButtonRepresentation("Описание", AdminCommand.ITEM_DESCRIPTION.name())),
                        Arrays.asList(
                                new ButtonRepresentation("Цена", AdminCommand.ITEM_PRICE.name()),
                                new ButtonRepresentation("Старая цена", AdminCommand.ITEM_OLD_PRICE.name())),
                        Arrays.asList(
                                new ButtonRepresentation("Наличие", AdminCommand.ITEM_STOCK.name())),
                        Arrays.asList(
                                new ButtonRepresentation("Назад", AdminCommand.BACK.name()))));
    }

    private static InlineKeyboardMarkup genericKeycboard(List<List<ButtonRepresentation>> buttons) {
        List<InlineKeyboardRow> rows = buttons.stream()
                .map(strRow -> new InlineKeyboardRow(
                        strRow.stream()
                                .map(btn -> InlineKeyboardButton.builder()
                                        .text(btn.name)
                                        .callbackData(btn.callback)
                                        .build())
                                .collect(Collectors.toList())))
                .collect(Collectors.toList());
        return new InlineKeyboardMarkup(rows);
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class ButtonRepresentation {
        private String name;
        private String callback;
    }
}
