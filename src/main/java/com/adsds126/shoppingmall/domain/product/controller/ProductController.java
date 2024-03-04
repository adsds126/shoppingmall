package com.adsds126.shoppingmall.domain.product.controller;

import com.adsds126.shoppingmall.domain.product.dto.ProductDto;
import com.adsds126.shoppingmall.domain.product.entity.Product;
import com.adsds126.shoppingmall.domain.product.mapper.ProductMapper;
import com.adsds126.shoppingmall.domain.product.repository.ProductRepository;
import com.adsds126.shoppingmall.domain.product.service.ProductService;
import com.adsds126.shoppingmall.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/products")
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;

    /**
     * 상품등록 api
     * param = productName, price
     */
    @PostMapping
    public ResponseEntity<?> postProduct(@RequestBody ProductDto.Post requestBody) {
        Product product = productService.post(productMapper.productDtoPostToProduct(requestBody));
        ProductDto.Response response = productMapper.productToProductDtoResponse(product);
        return ResponseEntity.ok().body(ApiResponse.ok("data",response));
    }
}
