package com.bipsqwake.anxios_shop_api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OrderResponseDto {
    private Status status;

    public enum Status {
        SUCCESS,
        CONFLICT,
        ERROR
    }
}
