package com.bipsqwake.anxios_shop_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@NoArgsConstructor
@ToString
@Getter
@Setter
public class ItemResponseDto {
    @JsonProperty
    private String id;
    @JsonProperty
    private String name;
    @JsonProperty
    private String description;
    @JsonProperty
    private String imgUrl;
    @JsonProperty
    private String intName;
    @JsonProperty
    private int price;
    @JsonProperty
    private int itemsLeft;
    @JsonProperty
    private int oldPrice;  
}
