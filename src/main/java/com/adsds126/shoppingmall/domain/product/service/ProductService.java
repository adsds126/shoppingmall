package com.adsds126.shoppingmall.domain.product.service;

import com.adsds126.shoppingmall.domain.product.entity.Product;
import com.adsds126.shoppingmall.domain.product.repository.ProductRepository;
import com.adsds126.shoppingmall.exception.BusinessLogicException;
import com.adsds126.shoppingmall.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Product post(Product product) {
        verifyExistProduct(product.getProductName());
        Product newProduct = Product.builder()
                .productName(product.getProductName())
                .price(product.getPrice())
                .build();
        return productRepository.save(newProduct);
    }
    public void verifyExistProduct(String productName) {
        productRepository.findProductByProductName(productName).ifPresent((e) ->{
            throw new BusinessLogicException(ExceptionCode.ALREADY_EXISTS_INFORMATION);
        });
    }
}
