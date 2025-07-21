package com.bipsqwake.anxios_shop_api.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ImageStorage {

    @Value("${appconfig.upload.dir}")
    private String storagePath;
    
    public void store(byte[] image, String filename) throws IOException {
        Path path = Paths.get(storagePath, filename);
        Files.createDirectories(path.getParent());
        InputStream imageStream = new ByteArrayInputStream(image);
        Files.copy(imageStream, path, StandardCopyOption.REPLACE_EXISTING);
    }

    public void removeFile(String filename) throws IOException {
        Path path = Paths.get(storagePath, filename);
        Files.deleteIfExists(path);
    }
}
