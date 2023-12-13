package com.campusconnect.ui.market.controller;

import com.campusconnect.domain.product.dto.ProductDto;
import com.campusconnect.domain.product.entity.Product;
import com.campusconnect.domain.security.RequiredScope;
import com.campusconnect.domain.security.SecurityScope;
import com.campusconnect.ui.market.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/")
    @RequiredScope(scope = SecurityScope.NONE)
    public ResponseEntity<Product> updateProduct(@RequestBody ProductDto productCreationInfo, @RequestParam("productId") UUID productId){
        return ResponseEntity.ok(productService.updateProduct(productCreationInfo, productId));
    }

//    @DeleteMapping("/")
//    @RequiredScope(scope = SecurityScope.NONE)
//    public ResponseEntity<Void> deleteProductById@RequestParam("productId") UUID productId){
//        productService.deleteProductById(productId);
//        return ResponseEntity.ok(null);
//    }
}




