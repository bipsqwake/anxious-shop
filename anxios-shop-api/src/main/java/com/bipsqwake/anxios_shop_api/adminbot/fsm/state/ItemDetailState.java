package com.bipsqwake.anxios_shop_api.adminbot.fsm.state;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.photo.PhotoSize;

import com.bipsqwake.anxios_shop_api.adminbot.AdminBotKeyboards;
import com.bipsqwake.anxios_shop_api.adminbot.AdminCommand;
import com.bipsqwake.anxios_shop_api.adminbot.Prompts;
import com.bipsqwake.anxios_shop_api.adminbot.fsm.StateContext;
import com.bipsqwake.anxios_shop_api.adminbot.fsm.StateException;
import com.bipsqwake.anxios_shop_api.service.AdminService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ItemDetailState implements State {

    private static final Set<AdminCommand> intCommands = Set.of(AdminCommand.ITEM_PRICE,
            AdminCommand.ITEM_OLD_PRICE,
            AdminCommand.ITEM_STOCK);

    @Autowired
    AdminService adminService;

    @Override
    public boolean isInitial() {
        return false;
    }

    @Override
    public StateName getName() {
        return StateName.ITEM_INFO_DETAILS_UPDATE;
    }

    @Override
    public void onEnter(SilentSender sender, StateContext context) throws StateException {
        SendMessage message = new SendMessage(context.getStringChatId(),
                Prompts.requestMessages.get(context.getItemDetailCommand()));
        message.setReplyMarkup(AdminBotKeyboards.getBackKeyboard());
        sender.execute(message);
    }

    @Override
    public StateName handleText(SilentSender sender, StateContext context, String text) throws StateException {
        if (context.getItemId() == null || context.getItemId().isBlank()) {
            throw new StateException("No item id in context");
        }
        Integer intInput = null;
        if (intCommands.contains(context.getItemDetailCommand())) {
            try {
                intInput = Integer.parseInt(text);
            } catch (NumberFormatException e) {
                sender.send(Prompts.NUMBER_REQUIRED, context.getUserId());
                return StateName.ITEM_INFO_DETAILS_UPDATE;
            }
        }
        boolean success = false;
        switch (context.getItemDetailCommand()) {
            case ITEM_NAME:
                success = adminService.updateName(context.getItemId(), text);
                break;
            case ITEM_DESCRIPTION:
                success = adminService.updateDescription(context.getItemId(), text);
                break;
            case ITEM_PRICE:
                success = adminService.updatePrice(context.getItemId(), intInput);
                break;
            case ITEM_OLD_PRICE:
                success = adminService.updateOldPrice(context.getItemId(), intInput);
                break;
            case ITEM_STOCK:
                success = adminService.updateStock(context.getItemId(), intInput);
                break;
            default:
                break;
        }
        if (success) {
            sender.send(Prompts.SUCCESSFULL_UPDATE, context.getUserId());
        } else {
            sender.send(Prompts.FAILED_UPDATE, context.getUserId());
        }
        return StateName.ITEM_INFO;
    }

    @Override
    public StateName handleCommand(SilentSender sender, StateContext context, AdminCommand command)
            throws StateException {
        if (command == AdminCommand.BACK) {
            context.setItemId(null);
            return StateName.START_STATE;
        } else {
            return StateName.STAY;
        }
    }

    @Override
    public StateName handlePhoto(SilentSender sender, StateContext context, List<PhotoSize> photos) {
        return StateName.STAY;
    }

}
