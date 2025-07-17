package com.bipsqwake.anxios_shop_api.adminbot.fsm.state;

import java.util.List;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.photo.PhotoSize;

import com.bipsqwake.anxios_shop_api.adminbot.AdminBotKeyboards;
import com.bipsqwake.anxios_shop_api.adminbot.AdminCommand;
import com.bipsqwake.anxios_shop_api.adminbot.fsm.StateContext;
import com.bipsqwake.anxios_shop_api.adminbot.fsm.StateException;

@Component
public class StartState implements State {

    @Override
    public boolean isInitial() {
        return true;
    }

    @Override
    public StateName getName() {
        return StateName.START_STATE;
    }

    @Override
    public void onEnter(SilentSender sender, StateContext context) {
        SendMessage message = new SendMessage(context.getStringChatId(), "Выберите действие");
        message.setReplyMarkup(AdminBotKeyboards.getRootKeyboard());
        sender.execute(message);
    }

    @Override
    public StateName handleText(SilentSender sender, StateContext context, String text) throws StateException {
        return StateName.STAY;
    }

    @Override
    public StateName handleCommand(SilentSender sender, StateContext context, AdminCommand command)
            throws StateException {
        switch (command) {
            case ITEM_INFO:
                return StateName.REQUESTED_ITEM_ID_STATE;
            default:
                return StateName.STAY;
        }
    }

    @Override
    public StateName handlePhoto(SilentSender sender, StateContext context, List<PhotoSize> photos) {
        return StateName.STAY;
    }

}
