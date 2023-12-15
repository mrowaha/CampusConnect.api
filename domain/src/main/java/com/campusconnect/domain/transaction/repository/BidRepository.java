package com.campusconnect.domain.transaction.repository;

import com.campusconnect.domain.transaction.entity.Bid;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BidRepository extends JpaRepository<Bid, UUID> {
}
