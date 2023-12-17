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
    // Unique identifier of the buyer
    private UUID buyerId;

    // Unique identifier of the seller
    private UUID sellerId;

    // Unique identifier of the product
    private UUID productId;

    // Price of the transaction
    private Double price;

    // Return date for the transaction (if applicable)
    private LocalDate returnDate;


}
