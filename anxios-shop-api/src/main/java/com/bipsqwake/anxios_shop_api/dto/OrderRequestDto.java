package com.bipsqwake.anxios_shop_api.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderRequestDto {
    
    @JsonProperty
    private List<OrderElement> items;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class OrderElement {
        @JsonProperty
        private String id;
        @JsonProperty
        private int count;
    }
}
