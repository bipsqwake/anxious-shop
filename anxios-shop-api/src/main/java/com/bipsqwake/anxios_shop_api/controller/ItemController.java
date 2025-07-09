package com.bipsqwake.anxios_shop_api.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
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
import com.bipsqwake.anxios_shop_api.entity.Item;
import com.bipsqwake.anxios_shop_api.repository.ItemRepository;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/item")
@Slf4j
public class ItemController {

    @Autowired
    private ItemRepository itemRepository;

    ModelMapper modelMapper = new ModelMapper();
    

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ItemResponseDto> getItemsList(@RequestParam boolean available) {
        if (available) {
            return modelMapper.map(itemRepository.findByItemsLeftGreaterThanOrderByIntName(0), new TypeToken<List<ItemResponseDto>>() {}.getType());
        }
        return modelMapper.map(itemRepository.findAll(), new TypeToken<List<ItemResponseDto>>() {}.getType());
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ItemResponseDto getItem(@PathVariable("id") String id) {
        Optional<Item> result = itemRepository.findById(id);
        if (result.isPresent()) {
            return modelMapper.map(result.get(), ItemResponseDto.class);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping(value = "/find", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, ItemResponseDto> getItems(@RequestParam List<String> ids) {
        List<Item> result = itemRepository.findAllById(ids);
        return result.stream().collect(Collectors.toMap(item -> item.getId(),
            item -> modelMapper.map(item, ItemResponseDto.class)));
    }

    // @PostMapping(value = "")
    // public ResponseEntity<?> postItem(@RequestBody ItemRequestDto item) {
    //     log.info(item.toString());
    //     Item toSave = modelMapper.map(item, Item.class);
    //     log.info(toSave.toString());
    //     itemRepository.save(toSave);
    //     return ResponseEntity.accepted().build();
    // }
}
