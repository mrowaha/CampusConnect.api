package com.campusconnect.ui.common.controller;

import com.campusconnect.ui.common.service.ProductTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product-tag")
public class ProductTagController {
    private final ProductTagService productTagService;

    @Autowired
    public ProductTagController(ProductTagService productTagService){
        this.productTagService = productTagService;
    }

    @GetMapping("/request")
    public ResponseEntity<?> requestProductTag(@RequestParam String tagName){
        String response = productTagService.requestProductTag(tagName);
        return ResponseEntity.ok(response);
    }
}
