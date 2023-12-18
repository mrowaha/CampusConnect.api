package com.campusconnect.ui.transaction.controller;

import com.campusconnect.domain.security.RequiredScope;
import com.campusconnect.domain.security.SecurityScope;
import com.campusconnect.domain.transaction.entity.Bid;
import com.campusconnect.domain.user.entity.Bilkenteer;
import com.campusconnect.ui.common.controller.SecureController;
import com.campusconnect.ui.transaction.entity.TransactionRequest;
import com.campusconnect.ui.transaction.service.TransactionService;
import com.campusconnect.ui.utils.UserUtilities;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController extends SecureController {

    private final TransactionService transactionService;
    private final UserUtilities userUtilities;

    @PostMapping("/cancelBid")
    @RequiredScope(scope = SecurityScope.BILKENTEER)
    public ResponseEntity<String> cancelBid(@RequestBody TransactionRequest request) {
        transactionService.cancelBid(request.getBuyerId(), request.getProductId());
        return new ResponseEntity<>("Bid canceled successfully", HttpStatus.OK);
    }

    @GetMapping("/bids")
    @RequiredScope(scope = SecurityScope.BILKENTEER)
    public ResponseEntity<Set<Bid>> getBid(
            Authentication authentication
    ) {
        Bilkenteer bilkenteer = (Bilkenteer) userUtilities.getUserFromAuth(authentication);
        return ResponseEntity.ok(transactionService.getAllBids(bilkenteer));
    }

    @PostMapping("/makeBid")
    @RequiredScope(scope = SecurityScope.BILKENTEER)
    public ResponseEntity<String> makeBid(
            Authentication authentication,
            @RequestBody TransactionRequest request
    ) {
        Bilkenteer creator = (Bilkenteer)userUtilities.getUserFromAuth(authentication);
        transactionService.makeBid(creator ,request.getProductId(), request.getPrice(), request.getReturnDate());
        return new ResponseEntity<>("Bid made successfully", HttpStatus.OK);
    }

    @PostMapping("/sellProduct")
    @RequiredScope(scope = SecurityScope.BILKENTEER)
    public ResponseEntity<String> sellProduct(@RequestBody TransactionRequest request) {
        transactionService.sellProduct(request.getBuyerId(), request.getSellerId(), request.getProductId());
        return new ResponseEntity<>("Product sold successfully", HttpStatus.OK);
    }

    @PostMapping("/rentProduct")
    @RequiredScope(scope = SecurityScope.BILKENTEER)
    public ResponseEntity<String> rentProduct(@RequestBody TransactionRequest request) {
        transactionService.rentProduct(request.getBuyerId(), request.getSellerId(), request.getProductId(), request.getReturnDate());
        return new ResponseEntity<>("Product rented successfully", HttpStatus.OK);
    }
}
