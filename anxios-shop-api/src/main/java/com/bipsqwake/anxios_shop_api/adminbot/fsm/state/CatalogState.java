package com.bipsqwake.anxios_shop_api.adminbot.fsm.state;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.photo.PhotoSize;

import com.bipsqwake.anxios_shop_api.adminbot.AdminBotKeyboards;
import com.bipsqwake.anxios_shop_api.adminbot.AdminCommand;
import com.bipsqwake.anxios_shop_api.adminbot.Utils;
import com.bipsqwake.anxios_shop_api.adminbot.fsm.StateException;
import com.bipsqwake.anxios_shop_api.adminbot.fsm.context.StateContext;
import com.bipsqwake.anxios_shop_api.dto.ItemResponseDto;
import com.bipsqwake.anxios_shop_api.service.ItemService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CatalogState implements State {

    @Autowired
    private ItemService itemService;

    @Override
    public boolean isInitial() {
        return false;
    }

    @Override
    public StateName getName() {
        return StateName.CATALOG;
    }

    @Override
    public void onEnter(SilentSender sender, StateContext context) throws StateException {
        context.setCatalogPage(0);
        showPage(sender, context);
    }

    @Override
    public StateName handleText(SilentSender sender, StateContext context, String text) throws StateException {
        return StateName.STAY;
    }

    @Override
    public StateName handleCommand(SilentSender sender, StateContext context, AdminCommand command)
            throws StateException {
        switch (command) {
            case PREV:
                context.setCatalogPage(context.getCatalogPage() - 1);
                showPage(sender, context);
                return StateName.STAY;
            case NEXT:
                context.setCatalogPage(context.getCatalogPage() + 1);
                showPage(sender, context);
                return StateName.STAY;
            case BACK:
                context.setItemCreateContext(null);
                return StateName.START;
            case EMPTY:
            default:
                break;
        }
        return StateName.STAY;
    }

    @Override
    public StateName handlePhoto(SilentSender sender, StateContext context, List<PhotoSize> photos)
            throws StateException {
        return StateName.STAY;
    }

    private void showPage(SilentSender sender, StateContext context) {
        Page<ItemResponseDto> items = itemService.getItemsPage(context.getCatalogPage());
        List<List<String>> rows = new ArrayList<>();
        rows.add(List.of("ID", "Название", "Остаток"));
        items.get().forEachOrdered(
                item -> rows.add(List.of(item.getIntName(), item.getName(), Integer.toString(item.getItemsLeft()))));
        int[] maxColWidths = { 15, 15, 7 };
        SendMessage msg = new SendMessage(context.getStringChatId(), Utils.getTextTable(rows, maxColWidths));
        msg.setParseMode("HTML");
        msg.setReplyMarkup(AdminBotKeyboards.getPagesKeyboard(items.hasPrevious(), items.hasNext()));
        sender.execute(msg);
    }

}
