package com.bipsqwake.anxios_shop_api.adminbot.fsm.state;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.photo.PhotoSize;

import com.bipsqwake.anxios_shop_api.adminbot.AdminBotKeyboards;
import com.bipsqwake.anxios_shop_api.adminbot.AdminCommand;
import com.bipsqwake.anxios_shop_api.adminbot.fsm.StateException;
import com.bipsqwake.anxios_shop_api.adminbot.fsm.context.StateContext;
import com.bipsqwake.anxios_shop_api.dto.ItemResponseDto;
import com.bipsqwake.anxios_shop_api.service.ItemService;

@Component
public class RequestItemIdState implements State {

    @Autowired
    private ItemService itemService;

    @Override
    public boolean isInitial() {
        return false;
    }

    @Override
    public StateName getName() {
        return StateName.REQUESTED_ITEM_ID;
    }

    @Override
    public void onEnter(SilentSender sender, StateContext context) {
        SendMessage message = new SendMessage(context.getStringChatId(), "Введите ID товара");
        message.setReplyMarkup(AdminBotKeyboards.getBackKeyboard());
        sender.execute(message);
    }

    @Override
    public StateName handleText(SilentSender sender, StateContext context, String text) throws StateException  {
        ItemResponseDto item = itemService.getItemByIntName(text);
        if (item == null) {
            sender.send("Такого товара нет", context.getChatId());
            return StateName.REQUESTED_ITEM_ID;
        } else {
            context.setItemId(text);
            return StateName.ITEM_INFO;
        }
    }

    @Override
    public StateName handleCommand(SilentSender sender, StateContext context, AdminCommand command) throws StateException  {
        switch (command) {
            case BACK:
                return StateName.START;
            default:
                return StateName.STAY;
        }
    }

    @Override
    public StateName handlePhoto(SilentSender sender, StateContext context, List<PhotoSize> photos) throws StateException {
        return StateName.STAY;
    }
    
}
