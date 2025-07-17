package com.bipsqwake.anxios_shop_api.adminbot.fsm.state;

import java.util.List;

import org.telegram.telegrambots.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.api.objects.photo.PhotoSize;

import com.bipsqwake.anxios_shop_api.adminbot.AdminCommand;
import com.bipsqwake.anxios_shop_api.adminbot.fsm.StateContext;
import com.bipsqwake.anxios_shop_api.adminbot.fsm.StateException;

public interface State {
    public boolean isInitial();
    public StateName getName();
    public void onEnter(SilentSender sender, StateContext context) throws StateException ;
    public StateName handleText(SilentSender sender, StateContext context, String text) throws StateException ;
    public StateName handleCommand(SilentSender sender, StateContext context, AdminCommand command) throws StateException ;
    public StateName handlePhoto(SilentSender sender, StateContext context, List<PhotoSize> photos);
}
