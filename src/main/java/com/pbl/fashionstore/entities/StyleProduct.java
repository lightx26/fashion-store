package com.pbl.fashionstore.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "style_product")
@Getter
@Setter
public class StyleProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "style_product_id")
    private Long id;

    @Column(name = "style_id")
    private Long styleId;

    @Column(name = "product_id")
    private Long productId;
}
