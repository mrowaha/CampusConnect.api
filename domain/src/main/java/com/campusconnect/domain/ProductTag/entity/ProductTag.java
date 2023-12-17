package com.campusconnect.domain.ProductTag.entity;

import com.campusconnect.domain.ProductTag.enums.ProductTagStatus;
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
@Table(name = "cc_product_tags")
public class ProductTag {
    @Id
    @Column(name = "tag_name", nullable = false)
    private String name;

    // The status of the product tag (e.g., REQUESTED, APPROVED, DISABLED)
    @Enumerated(EnumType.STRING)
    @Column(name = "tag_status", nullable = false)
    private ProductTagStatus tagStatus;

    // The UUID of the Bilkenteer who requested the tag
    @Column(name = "requested_by_id", nullable = true)
    private UUID requestedByID;

    // The UUID of the Moderator who accepted the tag (can be null if not accepted yet)
    @Column(name = "accepted_by_id", nullable = true)
    private UUID acceptedByID;

//    @Column(nullable = false)
//    private UUID categoriesID;

    // Accept and Decline methods would be service layer operations and not part of the JPA entity.
    // These operations would update the tagStatus field accordingly.
}
