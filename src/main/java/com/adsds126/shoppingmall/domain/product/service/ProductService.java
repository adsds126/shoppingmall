package com.adsds126.shoppingmall.domain.product.service;

import com.adsds126.shoppingmall.domain.member.entity.Member;
import com.adsds126.shoppingmall.domain.product.entity.Product;
import com.adsds126.shoppingmall.domain.product.repository.ProductRepository;
import com.adsds126.shoppingmall.exception.BusinessLogicException;
import com.adsds126.shoppingmall.exception.ExceptionCode;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    public Product findProduct(Long productId) {
        Optional<Product> product = productRepository.findById(productId);

        return product.orElseThrow(() -> new BusinessLogicException(ExceptionCode.PRODUCT_NOT_FOUND));
    }

    public void updateProduct(Long productId, String productName, int price) {
        Product existProduct = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("상품을 찾을 수 없습니다."));
        existProduct.setProductName(productName);
        existProduct.setPrice(price);
    }

    public void deleteProduct(Long productId) {
        Product existProduct = productRepository.findById(productId)
                .orElseThrow(()-> new EntityNotFoundException("사용차를 찾을 수 없습니다."));
        productRepository.delete(existProduct);
    }
}
