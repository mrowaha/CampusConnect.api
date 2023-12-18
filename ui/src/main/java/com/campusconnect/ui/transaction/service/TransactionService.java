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

import java.sql.BatchUpdateException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class TransactionService {
    // Repositories for database operations
    private final ProductRepository productRepository;
    private final BilkenteerRepository bilkenteerRepository;
    private final NotificationRepository notificationRepository;
    private final BidRepository bidRepository;
    private final TransactionRepository transactionRepository;

    /**
     * Cancels a bid made by a buyer on a product.
     *
     * @param buyerId  ID of the buyer.
     * @param productId ID of the product.
     * @throws UserNotFoundException     If the buyer is not found.
     * @throws ProductNotFoundException  If the product is not found.
     */
    public void cancelBid(UUID buyerId, UUID productId) {
        Bilkenteer bilkenteer = bilkenteerRepository.findById(buyerId).orElseThrow(UserNotFoundException::new);
        Product product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);

        Bid bidToRemove = null;
        for (Bid bid : product.getBids()) {
            if (bid.getCreatedBy().equals(bilkenteer)) {
                bidToRemove = bid;
                break;
            }
        }
        if (bidToRemove != null) {
            product.getBids().remove(bidToRemove);
            bidRepository.delete(bidToRemove);
        }
    }

    @Transactional
    public Set<Bid> getAllBids(Bilkenteer bilkenteer) {
        Set<Bid> bids = bilkenteer.getBids();
        System.out.println(bids);
        return bids;
    }


    /**
     * Places a bid on a product by a buyer and notifies the seller.
     *
     * @param productId  ID of the product.
     * @param bidPrice   Price offered in the bid.
     * @param returnDate Return date for the bid (for rental products).
     * @throws UserNotFoundException     If the buyer or seller is not found.
     * @throws ProductNotFoundException  If the product is not found.
     */
    public void makeBid(Bilkenteer creator, UUID productId, double bidPrice, LocalDate returnDate) {
        Product product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);
        Bilkenteer owner = product.getSeller();

        boolean isRentingProduct = product.getType() == ProductType.RENT;

        Period period = isRentingProduct ? Period.between(returnDate, LocalDate.now()) : null;

        Bid bid = Bid.builder()
                .createdBy(creator)
                .product(product)
                .requestedPrice(bidPrice)
                .period(period)
                .build();

        bidRepository.save(bid);
        notifyUser(owner, "New bid has been made on your product.");
    }
    /**
     * Notifies a user with a message.
     *
     * @param owner   User to be notified.
     * @param message Notification message.
     */
    private void notifyUser(Bilkenteer owner, String message) {
        Notification notification = new Notification();
        notification.setUser(owner);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setSeen(false);
        notification.setType(NotificationType.BID);
        notification.setContent(message);

        notificationRepository.save(notification);
    }

    /**
     * Sells a product, updating its status to SOLD, clearing bids, and notifying bidders.
     *
     * @param buyerId   ID of the buyer.
     * @param sellerId  ID of the seller.
     * @param productId ID of the product.
     * @throws UserNotFoundException        If the buyer or seller is not found.
     * @throws ProductNotFoundException     If the product is not found.
     * @throws ProductNotAvailableException If the product is not available for sale.
     */
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
    /**
     * Notifies bidders about the item they bid on is sold.
     *
     * @param bids    List of bids to notify.
     * @param message Notification message.
     */
    private void notifyBidders(List<Bid> bids, String message) {
        for (Bid bid : bids) {
            notifyUser(bid.getCreatedBy(),message);
        }
    }
    /**
     * Rents a product, updating its status, rental start and end dates.
     *
     * @param buyerId    ID of the buyer.
     * @param sellerId   ID of the seller.
     * @param productId  ID of the product.
     * @param returnDate Return date for the rental.
     * @throws ProductNotFoundException If the product is not found.
     */
    public void rentProduct(UUID buyerId, UUID sellerId, UUID productId, LocalDate returnDate) {
        Product product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);

        product.setRentalStartDate(LocalDate.now());

        product.setRentalEndDate(returnDate);

        product.setStatus(ProductStatus.RENTED);

        product.getBids().clear();

        productRepository.save(product);
    }

}
