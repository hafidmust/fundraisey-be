package com.fundraisey.backend.entity.startup;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fundraisey.backend.entity.DateProps;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "product_photo")
public class ProductPhoto extends DateProps implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true, name = "url")
    private String url;

    @ManyToOne(targetEntity = Product.class)
    @JoinColumn(name = "id_product", referencedColumnName = "id")
    @JsonBackReference
    private Product product;
}
