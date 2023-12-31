package com.campusconnect.ui.market.controller;

import com.campusconnect.domain.forumPost.entity.ForumPost;
import com.campusconnect.domain.product.dto.ProductDto;
import com.campusconnect.domain.product.dto.ProductImageCountDto;
import com.campusconnect.domain.product.dto.ProductSearchDto;
import com.campusconnect.domain.product.entity.Product;
import com.campusconnect.domain.security.RequiredScope;
import com.campusconnect.domain.security.SecurityScope;
import com.campusconnect.domain.transaction.entity.Bid;
import com.campusconnect.ui.market.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.Authenticator;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping()
    @RequiredScope(scope = SecurityScope.NONE)
    public ResponseEntity<?> saveProduct(@Valid @RequestBody ProductDto productCreationInfo) {
        return ResponseEntity.ok(productService.saveProduct(productCreationInfo));
    }

    @GetMapping()
    @RequiredScope(scope = SecurityScope.NONE)
    public ResponseEntity<List<Product>> fetchProductList(){
        return ResponseEntity.ok(productService.fetchProductList());
    }

    @GetMapping("/{productId}/bids")
    @RequiredScope(scope = SecurityScope.NONE)
    public ResponseEntity<List<Bid>> getProductBids(@PathVariable UUID productId) {
        List<Bid> productBids = productService.getProductBids(productId);
        return ResponseEntity.ok(productBids);
    }

    @GetMapping("/")
    @RequiredScope(scope = SecurityScope.NONE)
    public ResponseEntity<Product> fetchProductById(@RequestParam("productId") UUID productId){
        return ResponseEntity.ok(productService.fetchProductById(productId));
    }

    @GetMapping("/user/{userId}")
    @RequiredScope(scope = SecurityScope.NONE)
    public ResponseEntity<List<Product>> fetchProductsByUserId(@PathVariable UUID userId){
        return ResponseEntity.ok(productService.fetchProductsByUserId(userId));
    }

    @PutMapping("/")
    @RequiredScope(scope = SecurityScope.NONE)
    public ResponseEntity<Product> updateProduct(@RequestBody ProductDto productCreationInfo, @RequestParam("productId") UUID productId){
        return ResponseEntity.ok(productService.updateProduct(productCreationInfo, productId));
    }

    @PutMapping("/tag")
    @RequiredScope(scope = SecurityScope.NONE)
    public ResponseEntity<Product> assignTag(@RequestParam("tagName") String tagName, @RequestParam("productId") UUID productId){
        return ResponseEntity.ok(productService.assignTag(tagName, productId));
    }

    @DeleteMapping("/")
    @RequiredScope(scope = SecurityScope.NONE)
    public ResponseEntity<Void> deleteProductById(@RequestParam("productId") UUID productId){
        productService.deleteProductById(productId);
        return ResponseEntity.ok(null);
    }

    @PostMapping("/search")
    @RequiredScope(scope = SecurityScope.NONE)
    public ResponseEntity<List<Product>> searchProduct(@RequestBody ProductSearchDto productSearchDto) {
        return ResponseEntity.ok(productService.searchProduct(productSearchDto));
    }

    @GetMapping("/{productId}/images")
    @RequiredScope(scope = SecurityScope.NONE)
    public ResponseEntity<ProductImageCountDto> getImagesInfo(
            @PathVariable("productId") UUID productId
    ) {
        return ResponseEntity.ok(
                productService.getImagesInfo(productId)
        );
    }


}




