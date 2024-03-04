package com.adsds126.shoppingmall.domain.product.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ProductDto {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Post {
        @NotNull
        private String productName;

        @NotNull
        private int price;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Update {
        @NotNull
        private String productName;

        @NotNull
        private int price;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {
        @NotNull
        private long productId;

        @NotNull
        private String productName;

        @NotNull
        private int price;
    }

}
