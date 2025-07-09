package com.bipsqwake.anxios_shop_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ItemRequestDto {
    @JsonProperty
    String name;
    @JsonProperty
    String description;
    @JsonProperty
    String imgUrl;
    @JsonProperty
    int price;
    @JsonProperty
    int itemsLeft;
    @JsonProperty
    String intName;
    @JsonProperty
    int oldPrice;
}
