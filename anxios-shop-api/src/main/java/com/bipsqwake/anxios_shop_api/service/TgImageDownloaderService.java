package com.bipsqwake.anxios_shop_api.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.telegram.telegrambots.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.GetFile.GetFileBuilder;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.photo.PhotoSize;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TgImageDownloaderService {

    @Value("${appconfig.bot.token}")
    private String botToken;

    private final RestClient restClient;

    public TgImageDownloaderService(RestClient.Builder restClientBuilder) {
        restClient = restClientBuilder.build();
    }

    public byte[] getImage(SilentSender sender, PhotoSize photo) throws IOException {
        String fileId = photo.getFileId();
        GetFile getFileMethod = GetFile.builder().fileId(fileId).build();
        Optional<File> file = sender.execute(getFileMethod);
        if (!file.isPresent()) {
            throw new IOException("No file after executing getFile method");
        }
        String fileUrl = file.get().getFileUrl(botToken);
        return downloadImage(fileUrl);
    }

    private byte[] downloadImage(String url) {
        return restClient
                .get()
                .uri(url)
                .retrieve()
                .body(byte[].class);
    }
}
