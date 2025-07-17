package com.bipsqwake.anxios_shop_api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bipsqwake.anxios_shop_api.dto.ItemRequestDto;
import com.bipsqwake.anxios_shop_api.service.AdminService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/admin")
@Slf4j
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping(value = "/item")
    public ResponseEntity<?> postItem(@RequestBody ItemRequestDto item) {
        adminService.postItem(item);
        return ResponseEntity.accepted().build();
    }

    @PostMapping(value = "/items")
    public ResponseEntity<?> postItems(@RequestBody List<ItemRequestDto> items) {
        adminService.postItems(items);
        return ResponseEntity.accepted().build();
    }

    @GetMapping(value = "/clear")
    public ResponseEntity<?> clear() {
        adminService.clear();
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/return/{id}")
    public ResponseEntity<?> returnItem(@PathVariable("id") String intName) {
        boolean result = adminService.returnItem(intName);
        return result ? ResponseEntity.accepted().build() : ResponseEntity.notFound().build();
    }
}
