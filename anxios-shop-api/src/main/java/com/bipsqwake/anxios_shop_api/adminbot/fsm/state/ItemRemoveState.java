package com.bipsqwake.anxios_shop_api.adminbot.fsm.state;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.photo.PhotoSize;

import com.bipsqwake.anxios_shop_api.adminbot.AdminBotKeyboards;
import com.bipsqwake.anxios_shop_api.adminbot.AdminCommand;
import com.bipsqwake.anxios_shop_api.adminbot.Constants;
import com.bipsqwake.anxios_shop_api.adminbot.fsm.StateException;
import com.bipsqwake.anxios_shop_api.adminbot.fsm.context.StateContext;
import com.bipsqwake.anxios_shop_api.dto.ItemResponseDto;
import com.bipsqwake.anxios_shop_api.service.AdminService;
import com.bipsqwake.anxios_shop_api.service.ImageStorage;
import com.bipsqwake.anxios_shop_api.service.ItemService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ItemRemoveState implements State {

    @Autowired
    private ItemService itemService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private ImageStorage imageStorage;

    @Override
    public boolean isInitial() {
        return false;
    }

    @Override
    public StateName getName() {
        return StateName.REMOVE_ITEM;
    }

    @Override
    public void onEnter(SilentSender sender, StateContext context) throws StateException {
        ItemResponseDto item = validateItemInContext(context);

        SendMessage message = new SendMessage(context.getStringChatId(),
                String.format(Constants.CONFIRM_ITEM_REMOVE, item.getIntName(), item.getName()));
        message.setReplyMarkup(AdminBotKeyboards.getConfirmKeyboard());
        sender.execute(message);
    }

    @Override
    public StateName handleText(SilentSender sender, StateContext context, String text) throws StateException {
        return StateName.STAY;
    }

    @Override
    public StateName handleCommand(SilentSender sender, StateContext context, AdminCommand command)
            throws StateException {
        validateItemInContext(context);
        switch (command) {
            case YES:
                try {
                    adminService.removeItemByIntName(context.getItemId());
                    imageStorage.removeFile(context.getItemId() + Constants.JPG_FORMAT);
                } catch (IOException e) {
                    log.error("Error while removing item: " + e.getMessage());
                    sender.send(Constants.ERROR, context.getChatId());
                    return StateName.START;
                }
                sender.send(Constants.SUCCESSFULL_DELETE, context.getChatId());
                return StateName.START;
            default:
                return StateName.START;
        }
    }

    @Override
    public StateName handlePhoto(SilentSender sender, StateContext context, List<PhotoSize> photos)
            throws StateException {
        return StateName.STAY;
    }

    private ItemResponseDto validateItemInContext(StateContext context) throws StateException {
        if (context.getItemId() == null) {
            throw new StateException("No item id in request");
        }
        ItemResponseDto item = itemService.getItemByIntName(context.getItemId());
        if (item == null) {
            throw new StateException("No item with id " + context.getItemId());
        }
        return item;
    }

}
