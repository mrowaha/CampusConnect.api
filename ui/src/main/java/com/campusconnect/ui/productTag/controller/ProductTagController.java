package com.campusconnect.ui.productTag.controller;

import com.campusconnect.domain.ProductTag.dto.ProductTagDto;
import com.campusconnect.domain.ProductTag.dto.BatchProductTagsDto;
import com.campusconnect.domain.ProductTag.entity.ProductTag;
import com.campusconnect.domain.ProductTag.enums.ProductTagStatus;
import com.campusconnect.domain.security.RequiredScope;
import com.campusconnect.domain.security.SecurityScope;
import com.campusconnect.domain.user.entity.User;
import com.campusconnect.ui.common.controller.SecureController;
import com.campusconnect.ui.productTag.exceptions.TagAlreadyExistsException;
import com.campusconnect.ui.productTag.exceptions.TagNotFoundException;
import com.campusconnect.ui.productTag.service.ProductTagService;
import com.campusconnect.ui.utils.UserUtilities;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product-tags")
public class ProductTagController extends SecureController {

    private final ProductTagService productTagService;
    private final UserUtilities userUtilities;

    @PostMapping("/batch")
    @RequiredScope(scope = SecurityScope.MODERATOR)
    public ResponseEntity<List<ProductTag>> batchUploadTags(
            Authentication authentication,
            @Valid @RequestBody BatchProductTagsDto productTagsDto
    ) throws UserUtilities.AuthToUserException {
        User user  =  userUtilities.getUserFromAuth(authentication);
        return new ResponseEntity<>(productTagService.batchUploadTags(productTagsDto.getTagNames(), user.getUserId()),
            HttpStatus.CREATED
        );
    }

    @PostMapping
    @RequiredScope(scope = SecurityScope.BILKENTEER)
    public ResponseEntity<ProductTag> requestProductTag(
            Authentication authentication,
            @Valid @RequestBody ProductTagDto productTagDto
    ) throws TagAlreadyExistsException {
        User user = userUtilities.getUserFromAuth(authentication);
        productTagDto.setTagStatus(ProductTagStatus.REQUESTED);
        ProductTag productTag = productTagService.requestProductTag(productTagDto, user.getUserId());
        return ResponseEntity.ok(productTag);
    }

    @GetMapping
    @RequiredScope(scope = SecurityScope.MODERATOR) // only moderator can get all tags
    public ResponseEntity<List<ProductTag>> getAllTags() {
        List<ProductTag> productTags = productTagService.getAllTags();
        return ResponseEntity.ok(productTags);
    }

    @GetMapping("/requested")
    @RequiredScope(scope = SecurityScope.MODERATOR)
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
    public ResponseEntity<ProductTag> getProductTag(@RequestParam("tagName") String tagName)
        throws TagNotFoundException
    {
        return new ResponseEntity<>(
                productTagService.getProductTag(tagName),
                HttpStatus.OK);
    }

    @PutMapping("/approve")
    @RequiredScope(scope = SecurityScope.MODERATOR)
    public ResponseEntity<ProductTag> approveTag(
            Authentication authentication,
            @RequestParam("tagName") String tagName
    ) throws TagNotFoundException {
        User user = userUtilities.getUserFromAuth((authentication));
        ProductTag productTag = productTagService.approveTag(tagName, user.getUserId());
        return ResponseEntity.ok(productTag);
    }
}
