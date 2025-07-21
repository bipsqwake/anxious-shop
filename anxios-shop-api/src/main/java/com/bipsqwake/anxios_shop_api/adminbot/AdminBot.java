package com.bipsqwake.anxios_shop_api.adminbot;

import java.io.IOException;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.abilitybots.api.bot.AbilityBot;
import org.telegram.telegrambots.abilitybots.api.db.DBContext;
import org.telegram.telegrambots.abilitybots.api.objects.Ability;
import org.telegram.telegrambots.abilitybots.api.objects.Locality;
import org.telegram.telegrambots.abilitybots.api.objects.Privacy;
import org.telegram.telegrambots.abilitybots.api.objects.Reply;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.BotSession;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.AfterBotRegistration;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;

import com.bipsqwake.anxios_shop_api.adminbot.fsm.StateException;
import com.bipsqwake.anxios_shop_api.adminbot.fsm.StatesService;
import com.bipsqwake.anxios_shop_api.service.ImageStorage;
import com.bipsqwake.anxios_shop_api.service.TgImageDownloaderService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AdminBot extends AbilityBot implements SpringLongPollingBot {
    @Value("${appconfig.bot.token}")
    private String token;

    @Autowired
    private StatesService statesService;

    @Autowired
    private TgImageDownloaderService imageDownloader;

    @Autowired
    private ImageStorage imageStorage;

    private static String ERROR_MESSAGE = "Что-то пошло не так";

    @Autowired
    public AdminBot(@Value("${appconfig.bot.token}") String token,
            @Value("${appconfig.bot.name}") String name,
            DBContext customContext) {
        super(new OkHttpTelegramClient(token), name, customContext);
    }

    @AfterBotRegistration
    public void afterRegistration(BotSession botSession) {
        this.onRegister();
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this;
    }

    @Override
    public Set<Long> admins() {
        return Set.of(155699444L);
    }

    public Ability startBot() {
        return Ability
                .builder()
                .name("start")
                .info("Starts bot")
                .input(0)
                .locality(Locality.ALL)
                .privacy(Privacy.ADMIN)
                .action(ctx -> {
                    try {
                        statesService.start(silent, ctx.chatId());
                    } catch (StateException e) {
                        log.error(e.getMessage());
                        silent.send(ERROR_MESSAGE, ctx.chatId());
                    }
                })
                .build();
    }

    public Reply handleReply() {
        return Reply.of((bot, upd) -> {
            try {
                if (upd.hasCallbackQuery()) {
                    AdminCommand command;
                    try {
                        command = AdminCommand.valueOf(upd.getCallbackQuery().getData());
                        statesService.handleCommand(silent, upd.getCallbackQuery().getMessage().getChatId(), command);
                    } catch (IllegalArgumentException e) {
                        statesService.handleText(silent, upd.getCallbackQuery().getMessage().getChatId(), upd.getCallbackQuery().getData());
                    }
                    
                } else if (upd.hasMessage() && upd.getMessage().hasText() && !upd.getMessage().isCommand()) {
                    String text = upd.getMessage().getText();
                    statesService.handleText(silent, upd.getMessage().getChatId(), text);
                } else if (upd.hasMessage() && !upd.getMessage().hasText() && upd.getMessage().hasPhoto()) {
                    statesService.handlePhoto(silent, upd.getMessage().getChatId(), upd.getMessage().getPhoto());
                }
            } catch (StateException e) {
                log.error(e.getMessage());
                silent.send(ERROR_MESSAGE, upd.getMessage().getChatId());
                return;
            }
        }, upd -> upd.hasCallbackQuery()
                || (upd.hasMessage() && upd.getMessage().hasText() && !upd.getMessage().isCommand())
                || (upd.hasMessage() && upd.getMessage().hasPhoto()));
    }

    @Override
    public long creatorId() {
        return 155699444L;
    }

    @Override
    public String getBotToken() {
        return token;
    }
}
