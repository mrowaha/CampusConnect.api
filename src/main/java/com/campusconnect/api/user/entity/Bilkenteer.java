package com.campusconnect.api.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name="cc_bilkenteer")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bilkenteer extends User{

    @Column(name = "trust_score", precision = 3, scale = 1)
    private Integer trustScore = 4;

    @Column(name = "is_suspended")
    private Boolean isSuspended = false;
}
