package com.campusconnect.domain.transaction.entity;
import com.campusconnect.domain.product.entity.Product;
import com.campusconnect.domain.user.entity.Bilkenteer;
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

    @Id
    @GeneratedValue
    private UUID bidId;

    @ManyToOne
    @JoinColumn(name = "bilkenteer")
    @JsonBackReference
    private Bilkenteer bilkenteer;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonBackReference
    private Product product;

    private Double requestedPrice;

    @JoinColumn(name = "period_id")
    private Period period;
}

