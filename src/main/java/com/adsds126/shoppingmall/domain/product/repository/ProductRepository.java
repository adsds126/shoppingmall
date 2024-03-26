package com.adsds126.shoppingmall.domain.product.repository;

import com.adsds126.shoppingmall.domain.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findByProductId(long productId);
    Optional<Product> findProductByProductName(String productName);

    //Product findProductById(long productId);
}
