package com.bipsqwake.anxios_shop_api.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bipsqwake.anxios_shop_api.dto.ItemRequestDto;
import com.bipsqwake.anxios_shop_api.entity.Item;
import com.bipsqwake.anxios_shop_api.repository.ItemRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AdminService {
    @Autowired
    private ItemRepository itemRepository;
    
    ModelMapper modelMapper = new ModelMapper();

    public void postItem(ItemRequestDto item) {
        Item toSave = modelMapper.map(item, Item.class);
        itemRepository.save(toSave);
    }

    public void postItems(List<ItemRequestDto> items) {
        List<Item> toSave = modelMapper.map(items, new TypeToken<List<Item>>() {}.getType());
        itemRepository.saveAll(toSave);
    }

    public void clear() {
        itemRepository.deleteAll();
    }

    public boolean returnItem(String intName) {
        Optional<Item> toUpdate = itemRepository.findByIntName(intName);
        if (toUpdate.isPresent()) {
            toUpdate.get().setItemsLeft(1);
            itemRepository.save(toUpdate.get());
            return true;
        } else {
            return false;
        }
    }

    @Transactional
    public boolean updateName(String intName, String name) {
        Optional<Item> toUpdate = itemRepository.findByIntNameForUpdate(intName);
        if (!toUpdate.isPresent()) {
            return false;
        }
        Item item = toUpdate.get();
        item.setName(name);
        itemRepository.save(item);
        return true;
    }

    @Transactional
    public boolean updateDescription(String intName, String description) {
        Optional<Item> toUpdate = itemRepository.findByIntNameForUpdate(intName);
        if (!toUpdate.isPresent()) {
            return false;
        }
        Item item = toUpdate.get();
        item.setDescription(description);
        itemRepository.save(item);
        return true;
    }

    @Transactional
    public boolean updatePrice(String intName, int price) {
        Optional<Item> toUpdate = itemRepository.findByIntNameForUpdate(intName);
        if (!toUpdate.isPresent()) {
            return false;
        }
        Item item = toUpdate.get();
        item.setPrice(price);
        itemRepository.save(item);
        return true;
    }

    @Transactional
    public boolean updateOldPrice(String intName, int old) {
        Optional<Item> toUpdate = itemRepository.findByIntNameForUpdate(intName);
        if (!toUpdate.isPresent()) {
            return false;
        }
        Item item = toUpdate.get();
        item.setOldPrice(old);
        itemRepository.save(item);
        return true;
    }

    @Transactional
    public boolean updateStock(String intName, int stock) {
        Optional<Item> toUpdate = itemRepository.findByIntNameForUpdate(intName);
        if (!toUpdate.isPresent()) {
            return false;
        }
        Item item = toUpdate.get();
        item.setItemsLeft(stock);
        itemRepository.save(item);
        return true;
    }

    @Transactional
    public boolean updateImgUrl(String intName, String imgUrl) {
        Optional<Item> toUpdate = itemRepository.findByIntNameForUpdate(intName);
        if (!toUpdate.isPresent()) {
            return false;
        }
        Item item = toUpdate.get();
        item.setImgUrl(imgUrl);
        itemRepository.save(item);
        return true;
    }

    public List<String> getSold() {
        log.info("Here");
        log.info(itemRepository.findIntNameByItemsLeft(0).toString());
        return itemRepository.findIntNameByItemsLeft(0);
    }

    public void removeItemByIntName(String intName) {
        itemRepository.deleteByIntName(intName);
    }
}
