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
import com.bipsqwake.anxios_shop_api.adminbot.Constants;
import com.bipsqwake.anxios_shop_api.adminbot.fsm.StateException;
import com.bipsqwake.anxios_shop_api.adminbot.fsm.context.StateContext;
import com.bipsqwake.anxios_shop_api.dto.ItemResponseDto;
import com.bipsqwake.anxios_shop_api.service.ItemService;

@Component
public class ItemInfoState implements State {

    @Autowired
    private ItemService itemService;

    private static final Set<AdminCommand> availableCommands = Set.of(AdminCommand.ITEM_NAME,
            AdminCommand.ITEM_DESCRIPTION,
            AdminCommand.ITEM_PRICE,
            AdminCommand.ITEM_OLD_PRICE,
            AdminCommand.ITEM_STOCK,
            AdminCommand.ITEM_IMG,
            AdminCommand.ITEM_REMOVE,
            AdminCommand.BACK);

    @Override
    public boolean isInitial() {
        return false;
    }

    @Override
    public StateName getName() {
        return StateName.ITEM_INFO;
    }

    @Override
    public void onEnter(SilentSender sender, StateContext context) throws StateException {
        if (context.getItemId() == null || context.getItemId().isBlank()) {
            throw new StateException("No item ID in ITEM_INFO state");
        }
        ItemResponseDto item = itemService.getItemByIntName(context.getItemId());
        if (item == null) {
            throw new StateException(String.format("No item with id %s in db", context.getItemId()));
        }
        SendMessage message = new SendMessage(context.getStringChatId(), item.getFriendlyString());
        message.setReplyMarkup(AdminBotKeyboards.getItemKeyboard());
        sender.execute(message);
    }

    @Override
    public StateName handleText(SilentSender sender, StateContext context, String text) throws StateException {
        return StateName.STAY;
    }

    @Override
    public StateName handleCommand(SilentSender sender, StateContext context, AdminCommand command)
            throws StateException {
        if (!availableCommands.contains(command)) {
            return StateName.STAY;
        }
        switch (command) {
            case ITEM_IMG:
                sender.send(Constants.CANT_DO_THIS, context.getChatId());
                onEnter(sender, context);
                return StateName.STAY;
            case ITEM_NAME:
            case ITEM_DESCRIPTION:
            case ITEM_PRICE:
            case ITEM_OLD_PRICE:
            case ITEM_STOCK:
                context.setItemDetailCommand(command);
                return StateName.ITEM_INFO_DETAILS_UPDATE;
            case ITEM_REMOVE:
                return StateName.REMOVE_ITEM;
            case BACK:
                context.setItemId(null);
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
