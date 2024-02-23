package com.adsds126.shoppingmall.domain.cart.entity;

import com.adsds126.shoppingmall.domain.member.entity.Member;
import com.adsds126.shoppingmall.domain.product.entity.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CART")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CART_ID")
    private int cartId;

    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @Column(name = "QUANTITY", nullable = false)
    private int quantity;

    @Column(name = "TOTAL_PRICE", nullable = false)
    private int totalPrice;
}
