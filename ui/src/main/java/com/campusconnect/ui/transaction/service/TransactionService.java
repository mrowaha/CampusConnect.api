package com.campusconnect.ui.transaction.service;

import com.campusconnect.domain.notification.entity.Notification;
import com.campusconnect.domain.notification.enums.NotificationType;
import com.campusconnect.domain.notification.repository.NotificationRepository;
import com.campusconnect.domain.product.entity.Product;
import com.campusconnect.domain.product.enums.ProductStatus;
import com.campusconnect.domain.product.enums.ProductType;
import com.campusconnect.domain.product.repository.ProductRepository;
import com.campusconnect.domain.transaction.entity.Bid;
import com.campusconnect.domain.transaction.repository.BidRepository;
import com.campusconnect.domain.transaction.repository.TransactionRepository;
import com.campusconnect.domain.user.entity.Bilkenteer;
import com.campusconnect.domain.user.repository.BilkenteerRepository;
import com.campusconnect.ui.market.exceptions.ProductNotAvailableException;
import com.campusconnect.ui.market.exceptions.ProductNotFoundException;
import com.campusconnect.ui.user.exceptions.UserNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class TransactionService {
    private final ProductRepository productRepository;
    private final BilkenteerRepository bilkenteerRepository;
    private final NotificationRepository notificationRepository;

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
        Bilkenteer owner = bilkenteerRepository.findById(sellerId).orElseThrow(UserNotFoundException::new);

        boolean isRentingProduct = product.getType() == ProductType.RENT;

        Period period = isRentingProduct ? Period.between(returnDate, LocalDate.now()) : null;

        Bid bid = Bid.builder()
                .bilkenteer(bilkenteer)
                .product(product)
                .requestedPrice(bidPrice)
                .period(period)
                .build();

        bidRepository.save(bid);
        product.getBids().add(bid);
        notifyUser(owner, "New bid has been made on your product.");
    }

    private void notifyUser(Bilkenteer owner, String message) {
        Notification notification = new Notification();
        notification.setUser(owner);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setSeen(false);
        notification.setType(NotificationType.BID);
        notification.setContent(message);

        notificationRepository.save(notification);
    }


    public void sellProduct(UUID buyerId, UUID sellerId, UUID productId) {
        Bilkenteer buyer = bilkenteerRepository.findById(buyerId).orElseThrow(UserNotFoundException::new);
        Bilkenteer seller = bilkenteerRepository.findById(sellerId).orElseThrow(UserNotFoundException::new);
        Product product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);

        if (product.getStatus() == ProductStatus.AVAILABLE) {
            product.setStatus(ProductStatus.SOLD);

            notifyBidders(product.getBids(), "The product you bid on has been sold.");

            product.getBids().clear();
            productRepository.save(product);
            notifyBidders(product.getBids(), "The product you bid on has been sold.");
        } else {
            throw new ProductNotAvailableException("The product is not available for sale.");
        }
    }

    private void notifyBidders(List<Bid> bids, String message) {
        for (Bid bid : bids) {
            notifyUser(bid.getBilkenteer(),message);
        }
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
