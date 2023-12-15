package com.campusconnect.ui.transaction.controller;

import com.campusconnect.domain.security.RequiredScope;
import com.campusconnect.domain.security.SecurityScope;
import com.campusconnect.ui.transaction.entity.TransactionRequest;
import com.campusconnect.ui.transaction.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/cancelBid")
    public ResponseEntity<String> cancelBid(@RequestBody TransactionRequest request) {
        transactionService.cancelBid(request.getBuyerId(), request.getProductId());
        return new ResponseEntity<>("Bid canceled successfully", HttpStatus.OK);
    }

    @PostMapping("/makeBid")
    @RequiredScope(scope = SecurityScope.NONE)
    public ResponseEntity<String> makeBid(@RequestBody TransactionRequest request) {
        transactionService.makeBid(request.getBuyerId(), request.getSellerId() ,request.getProductId(), request.getPrice(), request.getReturnDate());
        return new ResponseEntity<>("Bid made successfully", HttpStatus.OK);
    }

    @PostMapping("/sellProduct")
    public ResponseEntity<String> sellProduct(@RequestBody TransactionRequest request) {
        transactionService.sellProduct(request.getBuyerId(), request.getSellerId(), request.getProductId());
        return new ResponseEntity<>("Product sold successfully", HttpStatus.OK);
    }

    @PostMapping("/rentProduct")
    public ResponseEntity<String> rentProduct(@RequestBody TransactionRequest request) {
        transactionService.rentProduct(request.getBuyerId(), request.getSellerId(), request.getProductId(), request.getReturnDate());
        return new ResponseEntity<>("Product rented successfully", HttpStatus.OK);
    }
}
