package com.adsds126.shoppingmall.domain.product.mapper;

import com.adsds126.shoppingmall.domain.member.entity.Member;
import com.adsds126.shoppingmall.domain.product.dto.ProductDto;
import com.adsds126.shoppingmall.domain.product.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {
    Product productDtoPostToProduct(ProductDto.Post requestBody);

    ProductDto.Response productToProductDtoResponse(Product product);
}
