package com.adsds126.shoppingmall.domain.product.controller;

import com.adsds126.shoppingmall.domain.product.dto.ProductDto;
import com.adsds126.shoppingmall.domain.product.entity.Product;
import com.adsds126.shoppingmall.domain.product.mapper.ProductMapper;
import com.adsds126.shoppingmall.domain.product.repository.ProductRepository;
import com.adsds126.shoppingmall.domain.product.service.ProductService;
import com.adsds126.shoppingmall.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    /**
     * 상품조회 api
     * param product-id
     */
    @GetMapping("/{product-id}")
    public ResponseEntity<?> getProduct(@PathVariable("product-id") Long productId) {
        Product product = productService.findProduct(productId);
        ProductDto.Response response = productMapper.productToProductDtoResponse(product);
        return ResponseEntity.ok().body(ApiResponse.ok("data", response));
    }
    /**
     * 상품수정 api
     */
    @PatchMapping("/{product-id}")
    public ResponseEntity<?> updateProduct(@PathVariable("product-id") Long productId, @RequestBody ProductDto.Update requestBody) {
        productService.updateProduct(productId, requestBody.getProductName(), requestBody.getPrice());
        Product product = productService.findProduct(productId);
        ProductDto.Response response = productMapper.productToProductDtoResponse(product);
        return ResponseEntity.ok().body(ApiResponse.ok("body", response));
    }
    /**
     * 상품삭제 api
     */
    @DeleteMapping("/{product-id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("product-id") Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.ok("상품삭제가 완료되었습니다.");
    }
}
