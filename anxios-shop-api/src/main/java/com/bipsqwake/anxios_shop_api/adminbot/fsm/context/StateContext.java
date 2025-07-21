package com.bipsqwake.anxios_shop_api.adminbot.fsm.context;

import com.bipsqwake.anxios_shop_api.adminbot.AdminCommand;
import com.bipsqwake.anxios_shop_api.adminbot.fsm.state.StateName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StateContext {
    private final Long chatId;
    private StateName state;
    private AdminCommand itemDetailCommand;
    private String prompt;
    private String itemId;
    private ItemCreateContext itemCreateContext;
    private int catalogPage;

    public StateContext(Long userId, StateName state) {
        this.chatId = userId;
        this.state = state;
    }

    public String getStringChatId() {
        return Long.toString(chatId);
    }
}
