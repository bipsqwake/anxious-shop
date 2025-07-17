package com.bipsqwake.anxios_shop_api.adminbot.fsm;

import com.bipsqwake.anxios_shop_api.adminbot.AdminCommand;
import com.bipsqwake.anxios_shop_api.adminbot.fsm.state.StateName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StateContext {
    private final Long userId;
    private StateName state;
    private AdminCommand itemDetailCommand;
    private String prompt;
    private String itemId;

    public StateContext(Long userId, StateName state) {
        this.userId = userId;
        this.state = state;
    }

    public String getStringChatId() {
        return Long.toString(userId);
    }
}
