package com.campusconnect.ui.productTag.controller;

import com.campusconnect.domain.ProductTag.dto.ProductTagDto;
import com.campusconnect.domain.ProductTag.entity.ProductTag;
import com.campusconnect.domain.security.RequiredScope;
import com.campusconnect.domain.security.SecurityScope;
import com.campusconnect.ui.common.controller.SecureController;
import com.campusconnect.ui.productTag.exceptions.TagNotFoundException;
import com.campusconnect.ui.productTag.service.ProductTagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/productTags")
public class ProductTagController extends SecureController {

    private final ProductTagService productTagService;

    @PostMapping()
    @RequiredScope(scope = SecurityScope.NONE)
    public ResponseEntity<ProductTag> requestProductTag(@Valid @RequestBody ProductTagDto productTagDto) {
        ProductTag productTag = productTagService.requestProductTag(productTagDto);
        return ResponseEntity.ok(productTag);
    }

    @GetMapping()
    @RequiredScope(scope = SecurityScope.NONE)
    public ResponseEntity<List<ProductTag>> getAllTags() {
        List<ProductTag> productTags = productTagService.getAllTags();
        return ResponseEntity.ok(productTags);
    }

    @GetMapping("/requested")
    @RequiredScope(scope = SecurityScope.NONE)
    public ResponseEntity<List<ProductTag>> getRequestedTags() {
        List<ProductTag> productTags = productTagService.getRequestedTags();
        return ResponseEntity.ok(productTags);
    }

    @GetMapping("/approved")
    @RequiredScope(scope = SecurityScope.NONE)
    public ResponseEntity<List<ProductTag>> getApprovedTags() {
        List<ProductTag> productTags = productTagService.getApprovedTags();
        return ResponseEntity.ok(productTags);
    }

    @GetMapping(value = "/tag")
    @RequiredScope(scope = SecurityScope.NONE)
    public ResponseEntity<ProductTag> getProductTag(@RequestParam("tagName") String tagName) {
        return new ResponseEntity<>(
                productTagService.getProductTag(tagName),
                HttpStatus.OK);
    }

    @PutMapping("/approve/")
    // For testing using security scope of none. Should be MODERATOR
    @RequiredScope(scope = SecurityScope.NONE)
    public ResponseEntity<ProductTag> approveTag(@RequestParam("tagName") String tagName) {
        ProductTag productTag = productTagService.approveTag(tagName);
        return ResponseEntity.ok(productTag);
    }
}
