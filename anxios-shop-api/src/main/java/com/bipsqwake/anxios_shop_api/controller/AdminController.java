package com.bipsqwake.anxios_shop_api.controller;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bipsqwake.anxios_shop_api.dto.ItemRequestDto;
import com.bipsqwake.anxios_shop_api.entity.Item;
import com.bipsqwake.anxios_shop_api.repository.ItemRepository;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/admin")
@Slf4j
public class AdminController {

    @Autowired
    private ItemRepository itemRepository;
    
    ModelMapper modelMapper = new ModelMapper();

    @PostMapping(value = "/item")
    public ResponseEntity<?> postItem(@RequestBody ItemRequestDto item) {
        log.info(item.toString());
        Item toSave = modelMapper.map(item, Item.class);
        log.info(toSave.toString());
        itemRepository.save(toSave);
        return ResponseEntity.accepted().build();
    }

    @PostMapping(value = "/items")
    public ResponseEntity<?> postItems(@RequestBody List<ItemRequestDto> items) {
        List<Item> toSave = modelMapper.map(items, new TypeToken<List<Item>>() {}.getType());
        itemRepository.saveAll(toSave);
        return ResponseEntity.accepted().build();
    }

    @GetMapping(value = "/clear")
    public ResponseEntity<?> clear() {
        itemRepository.deleteAll();
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/return/{id}")
    public ResponseEntity<?> returnItem(@PathVariable("id") String intName) {
        Optional<Item> toUpdate = itemRepository.findByIntName(intName);
        if (toUpdate.isPresent()) {
            toUpdate.get().setItemsLeft(1);
            itemRepository.save(toUpdate.get());
            return ResponseEntity.accepted().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
