package com.campusconnect.domain.transaction.repository;

import com.campusconnect.domain.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Product, UUID> {
}
