package com.bipsqwake.anxios_shop_api.adminbot.fsm.state;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.photo.PhotoSize;

import com.bipsqwake.anxios_shop_api.adminbot.AdminBotKeyboards;
import com.bipsqwake.anxios_shop_api.adminbot.AdminCommand;
import com.bipsqwake.anxios_shop_api.adminbot.fsm.StateException;
import com.bipsqwake.anxios_shop_api.adminbot.fsm.context.StateContext;
import com.bipsqwake.anxios_shop_api.service.AdminService;

@Component
public class SoldInfoState implements State {

    @Autowired
    private AdminService adminService;

    @Override
    public boolean isInitial() {
        return false;
    }

    @Override
    public StateName getName() {
        return StateName.SOLD_INFO;
    }

    @Override
    public void onEnter(SilentSender sender, StateContext context) throws StateException {
        String result = adminService.getSold().stream().collect(Collectors.joining("\n"));
        SendMessage msg = new SendMessage(context.getStringChatId(), result);
        msg.setReplyMarkup(AdminBotKeyboards.getBackKeyboard());
        sender.execute(msg);
    }

    @Override
    public StateName handleText(SilentSender sender, StateContext context, String text) throws StateException {
        return StateName.STAY;
    }

    @Override
    public StateName handleCommand(SilentSender sender, StateContext context, AdminCommand command)
            throws StateException {
        if (command == AdminCommand.BACK) {
            return StateName.START;
        }
        return StateName.STAY;
    }

    @Override
    public StateName handlePhoto(SilentSender sender, StateContext context, List<PhotoSize> photos) throws StateException {
        return StateName.STAY;
    }
    
}
