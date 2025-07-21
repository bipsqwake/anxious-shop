package com.bipsqwake.anxios_shop_api.adminbot.fsm.state;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.photo.PhotoSize;

import com.bipsqwake.anxios_shop_api.adminbot.AdminBotKeyboards;
import com.bipsqwake.anxios_shop_api.adminbot.AdminCommand;
import com.bipsqwake.anxios_shop_api.adminbot.Constants;
import com.bipsqwake.anxios_shop_api.adminbot.fsm.StateException;
import com.bipsqwake.anxios_shop_api.adminbot.fsm.context.ItemCreateContext;
import com.bipsqwake.anxios_shop_api.adminbot.fsm.context.StateContext;
import com.bipsqwake.anxios_shop_api.dto.ItemRequestDto;
import com.bipsqwake.anxios_shop_api.service.AdminService;
import com.bipsqwake.anxios_shop_api.service.ImageStorage;
import com.bipsqwake.anxios_shop_api.service.TgImageDownloaderService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ItemCreateState implements State {

    @Autowired
    private TgImageDownloaderService imageDownloader;

    @Autowired
    private ImageStorage imageStorage;

    @Autowired
    private AdminService adminService;

    @Value("${appconfig.upload.shared.path}")
    private String sharedPath;

    private static final Set<ItemCreateContext.CreateSubState> intRequiredState = Set.of(
            ItemCreateContext.CreateSubState.PRICE,
            ItemCreateContext.CreateSubState.OLD_PRICE,
            ItemCreateContext.CreateSubState.STOCK);

    private static final int IMAGE_SIZE_REQUEST_ROW_LENGTH = 3;
    private static final Pattern SIZE_PATTERN = Pattern.compile("\\d+x\\d+");
    private static final Pattern ID_PATTERN = Pattern.compile("[A-Za-z0-9_-]+");
    private static final String IMAGE_FORMAT = ".jpg";

    @Override
    public boolean isInitial() {
        return false;
    }

    @Override
    public StateName getName() {
        return StateName.ADD_ITEM;
    }

    @Override
    public void onEnter(SilentSender sender, StateContext context) throws StateException {
        ItemCreateContext createContext = new ItemCreateContext();
        createContext.setSubState(ItemCreateContext.CreateSubState.ID);
        context.setItemCreateContext(createContext);
        sendCurrentSubStateMessage(sender, context);
    }

    @Override
    public StateName handleText(SilentSender sender, StateContext context, String text) throws StateException {
        if (context.getItemCreateContext() == null) {
            throw new StateException("No create context in create state");
        }
        ItemCreateContext createContext = context.getItemCreateContext();
        Integer intText = null;
        if (intRequiredState.contains(createContext.getSubState())) {
            try {
                intText = Integer.parseInt(text);
            } catch (NumberFormatException e) {
                log.warn("Failed to parse int");
                sender.send(Constants.NUMBER_REQUIRED, context.getChatId());
                sendCurrentSubStateMessage(sender, context);
                return StateName.STAY;
            }
        }
        switch (context.getItemCreateContext().getSubState()) {
            case ID:
                if (!ID_PATTERN.matcher(text).matches()) {
                    sender.send(Constants.ID_PATTERN, context.getChatId());
                    sendCurrentSubStateMessage(sender, context);
                    break;
                }
                createContext.setIntId(text);
                createContext.setSubState(ItemCreateContext.CreateSubState.NAME);
                sendCurrentSubStateMessage(sender, context);
                break;
            case NAME:
                createContext.setName(text);
                createContext.setSubState(ItemCreateContext.CreateSubState.DESCRIPTION);
                sendCurrentSubStateMessage(sender, context);
                break;
            case DESCRIPTION:
                createContext.setDescription(text);
                createContext.setSubState(ItemCreateContext.CreateSubState.PRICE);
                sendCurrentSubStateMessage(sender, context);
                break;
            case PRICE:
                if (intText == null) {
                    sendCurrentSubStateMessage(sender, context);
                    return StateName.STAY;
                }
                createContext.setPrice(intText);
                createContext.setSubState(ItemCreateContext.CreateSubState.OLD_PRICE);
                sendCurrentSubStateMessage(sender, context);
                break;
            case OLD_PRICE:
                if (intText == null) {
                    sendCurrentSubStateMessage(sender, context);
                    return StateName.STAY;
                }
                createContext.setOldPrice(intText);
                createContext.setSubState(ItemCreateContext.CreateSubState.STOCK);
                sendCurrentSubStateMessage(sender, context);
                break;
            case STOCK:
                if (intText == null) {
                    sendCurrentSubStateMessage(sender, context);
                    return StateName.STAY;
                }
                createContext.setStock(intText);
                createContext.setSubState(ItemCreateContext.CreateSubState.IMG);
                sendCurrentSubStateMessage(sender, context);
                break;
            case IMG_SIZE:
                if (!validateImgSizeFormat(text)) {
                    sender.send(Constants.ERROR, context.getChatId());
                    sendCurrentSubStateMessage(sender, context);
                    return StateName.STAY;
                }
                PhotoSize photoSize = getPhoto(text, createContext);
                if (photoSize == null) {
                    sender.send(Constants.ERROR, context.getChatId());
                    sendCurrentSubStateMessage(sender, context);
                    return StateName.STAY;
                }
                createContext.setSelectedPhoto(photoSize);
                try {
                    createItem(sender, createContext);
                    sender.send(Constants.SUCCESSFULL_UPDATE, context.getChatId());
                    context.setItemCreateContext(null);
                    return StateName.START;
                } catch (IOException e) {
                    sender.send(Constants.ERROR, context.getChatId());
                    context.setItemCreateContext(null);
                    return StateName.START;
                }
            default:
                return StateName.STAY;
        }
        return StateName.STAY;
    }

    @Override
    public StateName handleCommand(SilentSender sender, StateContext context, AdminCommand command)
            throws StateException {
        if (command == AdminCommand.BACK) {
            context.setItemCreateContext(null);
            return StateName.START;
        }
        return StateName.STAY;
    }

    @Override
    public StateName handlePhoto(SilentSender sender, StateContext context, List<PhotoSize> photos)
            throws StateException {
        if (context.getChatId() == null) {
            throw new StateException("No userId in context");
        }
        if (context.getItemCreateContext() == null) {
            throw new StateException("No create context in create state");
        }
        if (context.getItemCreateContext().getSubState() != ItemCreateContext.CreateSubState.IMG) {
            log.info("Can handle photo only in IMG substate");
            return StateName.STAY;
        }
        context.getItemCreateContext().setPhotos(photos);
        context.getItemCreateContext().setSubState(ItemCreateContext.CreateSubState.IMG_SIZE);
        List<String> sizeList = photos.stream().map(this::getStrSize).toList();
        SendMessage msg = new SendMessage(context.getStringChatId(),
                Constants.createMessages.get(ItemCreateContext.CreateSubState.IMG_SIZE));
        msg.setReplyMarkup(AdminBotKeyboards.getCustomRowKeyboardWithBack(sizeList, IMAGE_SIZE_REQUEST_ROW_LENGTH));
        sender.execute(msg);
        return StateName.STAY;
    }

    private void sendCurrentSubStateMessage(SilentSender sender, StateContext context) throws StateException {
        if (context.getChatId() == null) {
            throw new StateException("No userId in context");
        }
        if (context.getItemCreateContext() == null) {
            throw new StateException("No create context in create state");
        }
        if (!Constants.createMessages.containsKey(context.getItemCreateContext().getSubState())) {
            throw new StateException("No prompt for substate " + context.getItemCreateContext().getSubState());
        }
        SendMessage message = new SendMessage(context.getStringChatId(),
                Constants.createMessages.get(context.getItemCreateContext().getSubState()));
        message.setReplyMarkup(AdminBotKeyboards.getBackKeyboard());
        sender.execute(message);
    }

    private boolean validateImgSizeFormat(String size) {
        return SIZE_PATTERN.matcher(size).matches();
    }

    private PhotoSize getPhoto(String size, ItemCreateContext context) {
        return context.getPhotos()
                .stream()
                .filter(ps -> size.equals(getStrSize(ps)))
                .findFirst()
                .orElse(null);
    }

    private String getStrSize(PhotoSize photoSize) {
        return photoSize.getWidth() + "x" + photoSize.getHeight();
    }

    //TODO: should not create image if db create failure
    private void createItem(SilentSender sender, ItemCreateContext context) throws IOException {
        String imgUrl = createImage(sender, context);
        context.setImgUrl(imgUrl);
        createDbItem(context);
    }

    private String createImage(SilentSender sender, ItemCreateContext context) throws IOException {
        byte[] imageBytes = imageDownloader.getImage(sender, context.getSelectedPhoto());
        imageStorage.store(imageBytes, context.getIntId() + IMAGE_FORMAT);
        return sharedPath + context.getIntId() + IMAGE_FORMAT;
    }

    private void createDbItem(ItemCreateContext context) {
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setIntName(context.getIntId());
        itemRequestDto.setName(context.getName());
        itemRequestDto.setDescription(context.getDescription());
        itemRequestDto.setPrice(context.getPrice());
        itemRequestDto.setOldPrice(context.getOldPrice());
        itemRequestDto.setImgUrl(context.getImgUrl());
        itemRequestDto.setItemsLeft(context.getStock());
        adminService.postItem(itemRequestDto);
    }

}
