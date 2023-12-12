package com.campusconnect.ui.user.controller;

import com.campusconnect.ui.user.service.ProductTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.campusconnect.domain.security.RequiredScope;
import com.campusconnect.domain.security.SecurityScope;
import com.campusconnect.ui.common.controller.SecureController;
import com.campusconnect.domain.user.dto.TagRequestDto;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import javax.swing.text.html.HTML;

@RestController
@RequestMapping("/product-tag")
public class ProductTagController extends SecureController {

    @Autowired
    ProductTagService productTagService;

    @PostMapping
    @RequiredScope(scope = SecurityScope.BILKENTEER)
    public ResponseEntity<?> createProductTag(@RequestBody TagRequestDto tagRequestDto) {
        System.out.println("In tag post");
        return ResponseEntity.ok(productTagService.createProductTag(tagRequestDto));
    }
}
