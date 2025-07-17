package com.bipsqwake.anxios_shop_api.adminbot.fsm;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.abilitybots.api.sender.SilentSender;

import com.bipsqwake.anxios_shop_api.adminbot.AdminCommand;
import com.bipsqwake.anxios_shop_api.adminbot.fsm.state.State;
import com.bipsqwake.anxios_shop_api.adminbot.fsm.state.StateName;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class StatesService {

    private final Map<Long, StateContext> states = new ConcurrentHashMap<>();
    private final Map<StateName, State> stateBeans;
    private final StateName initialState;

    @Autowired
    public StatesService(List<State> states) {
        if (states.isEmpty()) {
            throw new IllegalArgumentException("Should be at least one state");
        }
        this.stateBeans = states.stream().collect(Collectors.toMap(
                state -> state.getName(),
                Function.identity()));
        log.info(stateBeans.toString());
        initialState = StateName.START_STATE;
    }

    public void start(SilentSender sender, Long chatId) throws StateException {
        StateContext context = new StateContext(chatId, initialState);
        states.put(chatId, context);
        stateBeans.get(initialState).onEnter(sender, context);
    }

    private void setState(SilentSender sender, StateContext context, StateName stateName) throws StateException {
        State toSet = stateBeans.get(stateName);
        if (toSet == null) {
            throw new StateException("Invalid state name " + stateName);
        }
        context.setState(stateName);
        toSet.onEnter(sender, context);
    }

    public void handleText(SilentSender sender, Long chatId, String text) throws StateException {
        StateContext context = states.get(chatId);
        State state = stateBeans.get(context.getState());
        if (state == null) {
            throw new StateException("Invalid state name");
        }
        StateName toSet = state.handleText(sender, context, text);
        if (toSet != StateName.STAY) {
            setState(sender, context, toSet);
        }
    }

    public void handleCommand(SilentSender sender, Long chatId, AdminCommand command) throws StateException {
        StateContext context = states.get(chatId);
        State state = stateBeans.get(context.getState());
        if (state == null) {
            throw new StateException("Invalid state name");
        }
        StateName toSet = state.handleCommand(sender, context, command);
        if (toSet != StateName.STAY) {
            setState(sender, context, toSet);
        }
    }
}
