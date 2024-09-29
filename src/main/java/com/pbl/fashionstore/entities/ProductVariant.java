package com.pbl.fashionstore.entities;

import com.pbl.fashionstore.enums.ProductSize;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "product_variant")
@Getter
@Setter
public class ProductVariant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_variant_id")
    private Long id;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "color_id")
    private Long colorId;

    @Column(name = "size")
    private ProductSize size;

    @Column(name = "stock_count")
    private Integer stockCount;
}
