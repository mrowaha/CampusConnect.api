package com.campusconnect.ui.productTag.service;

import com.campusconnect.domain.ProductTag.dto.ProductTagDto;
import com.campusconnect.domain.ProductTag.entity.ProductTag;
import com.campusconnect.domain.ProductTag.enums.ProductTagStatus;
import com.campusconnect.domain.ProductTag.repository.ProductTagRepository;
import com.campusconnect.ui.productTag.exceptions.TagAlreadyExistsException;
import com.campusconnect.ui.productTag.exceptions.TagNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductTagService {
    private final ProductTagRepository productTagRepository;

    public ProductTag requestProductTag(ProductTagDto requestedTag) {
        Optional<ProductTag> existingTag = productTagRepository.findByName(requestedTag.getName());
        if (existingTag.isPresent()) {
            throw new TagAlreadyExistsException();
        }
        ProductTag productTag = new ProductTag();
        productTag.setName(requestedTag.getName());
        productTag.setTagStatus(ProductTagStatus.REQUESTED);
        productTag.setRequestedByID(requestedTag.getRequestedByID());
        return productTagRepository.save(productTag);
    }

    public List<ProductTag> getAllTags() {
        return productTagRepository.findAll();
    }

    public List<ProductTag> getRequestedTags() {
        return productTagRepository.getRequestedTags();
    }

    public List<ProductTag> getApprovedTags() {
        return productTagRepository.getApprovedTags();
    }

    public ProductTag getProductTag(String tagName) throws TagNotFoundException {
        return productTagRepository.findByName(tagName).orElseThrow(TagNotFoundException::new);
    }

    public ProductTag approveTag(String tagName) {
        Optional<ProductTag> optionalProductTag = productTagRepository.findByName(tagName);
        if (optionalProductTag.isPresent()) {
            ProductTag productTag = optionalProductTag.get();
            productTag.setTagStatus(ProductTagStatus.APPROVED);
            return productTagRepository.save(productTag);
        } else {
            throw new TagNotFoundException();
        }
    }
}
