package com.pbl.fashionstore.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "product_discount")
@Getter
@Setter
public class ProductDiscount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_discount_id")
    private Long id;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "discount_offer_id")
    private Long discountOfferId;
}
