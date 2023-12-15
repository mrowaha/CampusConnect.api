package com.campusconnect.ui.transaction.service;

import com.campusconnect.domain.product.entity.Product;
import com.campusconnect.domain.product.enums.ProductStatus;
import com.campusconnect.domain.product.repository.ProductRepository;
import com.campusconnect.domain.transaction.entity.Bid;
import com.campusconnect.domain.transaction.repository.BidRepository;
import com.campusconnect.domain.transaction.repository.TransactionRepository;
import com.campusconnect.domain.user.entity.Bilkenteer;
import com.campusconnect.domain.user.repository.BilkenteerRepository;
import com.campusconnect.ui.market.exceptions.ProductNotFoundException;
import com.campusconnect.ui.user.exceptions.UserNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class TransactionService {
    private final ProductRepository productRepository;
    private final BilkenteerRepository bilkenteerRepository;

    private final BidRepository bidRepository;
    private final TransactionRepository transactionRepository;

    public void cancelBid(UUID buyerId, UUID productId) {
        Bilkenteer bilkenteer = bilkenteerRepository.findById(buyerId).orElseThrow(UserNotFoundException::new);
        Product product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);

        Bid bidToRemove = null;
        for (Bid bid : product.getBids()) {
            if (bid.getBilkenteer().equals(bilkenteer)) {
                bidToRemove = bid;
                break;
            }
        }
        if (bidToRemove != null) {
            product.getBids().remove(bidToRemove);
            bidRepository.delete(bidToRemove);
        }
    }

    public void makeBid(UUID buyerId, UUID sellerId, UUID productId, double bidPrice, LocalDate returnDate) {

        Bilkenteer bilkenteer = bilkenteerRepository.findById(buyerId).orElseThrow(UserNotFoundException::new);
        Product product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);

        Bid bid = Bid.builder().bilkenteer(bilkenteer).product(product)
                .requestedPrice(bidPrice).period(Period.between(returnDate,LocalDate.now())).build();

        bidRepository.save(bid);
        product.getBids().add(bid);
    }

    public void sellProduct(UUID buyerId, UUID sellerId, UUID productId) {
        Bilkenteer buyer = bilkenteerRepository.findById(buyerId).orElseThrow(UserNotFoundException::new);
        Bilkenteer seller = bilkenteerRepository.findById(sellerId).orElseThrow(UserNotFoundException::new);
        Product product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);

        product.setStatus(ProductStatus.SOLD);
        product.getBids().clear();
        productRepository.save(product);
    }

    public void rentProduct(UUID buyerId, UUID sellerId, UUID productId, LocalDate returnDate) {
        Product product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);

        product.setRentalStartDate(LocalDate.now());

        product.setRentalEndDate(returnDate);

        product.setStatus(ProductStatus.RENTED);

        product.getBids().clear();

        productRepository.save(product);
    }

}
