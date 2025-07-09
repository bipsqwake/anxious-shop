package com.bipsqwake.anxios_shop_api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bipsqwake.anxios_shop_api.dto.OrderRequestDto;
import com.bipsqwake.anxios_shop_api.entity.Item;
import com.bipsqwake.anxios_shop_api.repository.ItemRepository;

import jakarta.transaction.Transactional;

@Service
public class OrderService {
    @Autowired
    private ItemRepository itemRepository;

    @Transactional
    public boolean placeOrder(List<OrderRequestDto.OrderElement> items) {
        for (OrderRequestDto.OrderElement orderElement : items) {
            Item item = itemRepository.findByIdForUpdate(orderElement.getId());
            if (item == null) {
                throw new RuntimeException("Can't find item " + orderElement.getId());
            }
            if (item.getItemsLeft() < orderElement.getCount()) {
                return false;
            }
            item.setItemsLeft(item.getItemsLeft() - orderElement.getCount());
            itemRepository.save(item);
        }
        return true;
    }
}
