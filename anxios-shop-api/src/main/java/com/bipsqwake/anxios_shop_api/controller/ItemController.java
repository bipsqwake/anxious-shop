package com.bipsqwake.anxios_shop_api.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.bipsqwake.anxios_shop_api.dto.ItemResponseDto;
import com.bipsqwake.anxios_shop_api.service.ItemService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/item")
@Slf4j
public class ItemController {

    @Autowired
    ItemService itemService;
    

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ItemResponseDto> getItemsList(@RequestParam boolean available) {
        return itemService.getItemsList(available);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ItemResponseDto getItem(@PathVariable("id") String id) {
        ItemResponseDto result = itemService.getItem(id);
        if (result == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return result;
    }


    @GetMapping(value = "/find", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, ItemResponseDto> getItems(@RequestParam List<String> ids) {
        return itemService.getItems(ids);
    }
}
