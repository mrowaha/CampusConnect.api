package com.campusconnect.api.user.entity;

import java.util.UUID;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="cc_bilkenteer_address")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BilkenteerAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "postal_code")
    @NonNull
    private String postalCode;

    @Column(name = "district")
    @NonNull
    private String district;

    @Column(name = "city")
    @NonNull
    private String city;

    @OneToOne(mappedBy = "address")
    private Bilkenteer bilkenteer;
}
