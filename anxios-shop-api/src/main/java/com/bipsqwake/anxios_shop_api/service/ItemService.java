package com.bipsqwake.anxios_shop_api.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.bipsqwake.anxios_shop_api.dto.ItemResponseDto;
import com.bipsqwake.anxios_shop_api.entity.Item;
import com.bipsqwake.anxios_shop_api.repository.ItemRepository;

@Service
public class ItemService {
    
    @Autowired
    private ItemRepository itemRepository;

    private static final int PAGE_SIZE = 10;

    ModelMapper modelMapper = new ModelMapper();

    public List<ItemResponseDto> getItemsList(boolean available) {
        if (available) {
            return modelMapper.map(itemRepository.findByItemsLeftGreaterThanOrderByIntName(0), new TypeToken<List<ItemResponseDto>>() {}.getType());
        }
        return modelMapper.map(itemRepository.findAll(), new TypeToken<List<ItemResponseDto>>() {}.getType());
    }

    public ItemResponseDto getItem(String id) {
        Optional<Item> result = itemRepository.findById(id);
        if (result.isPresent()) {
            return modelMapper.map(result.get(), ItemResponseDto.class);
        } else {
            return null;
        }
    }

    public Map<String, ItemResponseDto> getItems(List<String> ids) {
        List<Item> result = itemRepository.findAllById(ids);
        return result.stream().collect(Collectors.toMap(item -> item.getId(),
            item -> modelMapper.map(item, ItemResponseDto.class)));
    }

    public ItemResponseDto getItemByIntName(String intName) {
        Optional<Item> result = itemRepository.findByIntName(intName);
        return result.isPresent() ? modelMapper.map(result.get(), ItemResponseDto.class) : null;
    }

    public Page<ItemResponseDto> getItemsPage(int pageNum) {
        Pageable request = PageRequest.of(pageNum, PAGE_SIZE, Sort.by("intName"));
        Page<Item> items = itemRepository.findAll(request);
        return items.map(item -> modelMapper.map(item, ItemResponseDto.class));
    }
}
