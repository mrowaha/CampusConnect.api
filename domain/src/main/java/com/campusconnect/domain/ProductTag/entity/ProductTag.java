package com.campusconnect.domain.ProductTag.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "cc_product_tag")
public class ProductTag {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    protected UUID id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductTagStatus tagStatus;

    @Column(nullable = false)
    private UUID requestedByID;

    @Column
    private UUID acceptedByID;

    @Column(nullable = false)
    private UUID categoriesID;

    // Accept and Decline methods would be service layer operations and not part of the JPA entity.
    // These operations would update the tagStatus field accordingly.
}
