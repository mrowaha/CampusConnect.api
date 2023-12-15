package com.campusconnect.ui.transaction.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.Period;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionRequest {
    private UUID buyerId;
    private UUID sellerId;
    private UUID productId;
    private Double price;
    private LocalDate returnDate;

}
