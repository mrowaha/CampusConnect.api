package com.campusconnect.ui.market.controller;

import com.campusconnect.ui.market.dto.ProductDto;
import com.campusconnect.ui.market.entity.Product;
import com.campusconnect.ui.market.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Controller
public class ProductController {

    private final ProductService productService;

    @PostMapping("/products")
    public ResponseEntity<?> saveProduct(@Valid @RequestBody ProductDto productCreationInfo) {
        return productService.saveProduct(productCreationInfo);
    }

    @GetMapping("/products")
    public List<Product> fetchProductList(){
        return productService.fetchProductList();
    }

    @PutMapping("/products/{id}")
    public Product updateProduct(@RequestBody ProductDto productCreationInfo, @PathVariable("id") Long productId){
        return productService.updateProduct(productCreationInfo, productId);
    }

    @DeleteMapping("/products/{id}")
    public String deleteProductById(@PathVariable("id") Long productId)
    {
        productService.deleteProductById(productId);
        return "Deleted Successfully";
    }
}




