package com.campusconnect.domain.transaction.entity;
import com.campusconnect.domain.product.entity.Product;
import com.campusconnect.domain.user.entity.Bilkenteer;
import com.campusconnect.domain.user.entity.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Period;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Bid {

    // Unique identifier for the bid
    @Id
    @GeneratedValue
    private UUID bidId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bider_id", nullable = false)
    private Bilkenteer createdBy;

    // The product for which the bid is placed
    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonBackReference
    private Product product;

    // The price requested by the bidder in the bid
    private Double requestedPrice;

    // The period associated with the bid(if applicable)
    @JoinColumn(name = "period_id")
    private Period period;
}

