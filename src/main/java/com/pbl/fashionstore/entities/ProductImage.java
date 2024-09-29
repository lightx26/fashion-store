package com.pbl.fashionstore.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "product_image")
@Getter
@Setter
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_image_id")
    private Long id;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "product_variant_id")
    private Long productVariantId;

    @Column(name = "position")
    private Integer position;
}
