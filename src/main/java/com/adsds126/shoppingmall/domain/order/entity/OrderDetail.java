package com.adsds126.shoppingmall.domain.order.entity;


import com.adsds126.shoppingmall.domain.product.entity.Product;
import jakarta.persistence.*;

@Entity
@Table(name = "ORDER_DETAILS")
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORDER_DETAIL_ID")
    private int orderDetailId;

    @ManyToOne
    @JoinColumn(name = "ORDER_ID", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID", nullable = false)
    private Product product;

    @Column(name = "QUANTITY", nullable = false)
    private int quantity;

    @Column(name = "UNIT_PRICE", nullable = false)
    private int unitPrice;

    @Column(name = "TOTAL_PRICE", nullable = false)
    private int totalPrice;

}
