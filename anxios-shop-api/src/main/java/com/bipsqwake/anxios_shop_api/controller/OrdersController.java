package com.bipsqwake.anxios_shop_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bipsqwake.anxios_shop_api.dto.OrderRequestDto;
import com.bipsqwake.anxios_shop_api.dto.OrderResponseDto;
import com.bipsqwake.anxios_shop_api.dto.OrderResponseDto.Status;
import com.bipsqwake.anxios_shop_api.service.OrderService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrdersController {
    
    @Autowired
    private OrderService orderService;

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public OrderResponseDto placeOrder(@RequestBody OrderRequestDto request) {
        log.info("Order request: {}", request);
        try {
            boolean placed = orderService.placeOrder(request.getItems());
            return new OrderResponseDto(placed ? Status.SUCCESS : Status.CONFLICT);
        } catch (RuntimeException e) {
            log.info("Failed to place order: {}", e.getMessage());
            return new OrderResponseDto(Status.ERROR);
        }
    }
}
