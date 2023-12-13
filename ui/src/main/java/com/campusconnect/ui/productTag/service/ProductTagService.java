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

@Service
@Transactional
@RequiredArgsConstructor
public class ProductTagService {
    private final ProductTagRepository productTagRepository;

    public ProductTag requestProductTag(ProductTagDto requestedTag) {
        Optional<ProductTag> existingTag = productTagRepository.findByName(requestedTag.getName());
        if (existingTag.isPresent()) {
//            throw new IllegalStateException("A tag with the name '" + requestedTag.getName() + "' already exists.");
            throw new TagAlreadyExistsException();
        } else {
            ProductTag productTag = new ProductTag();
            productTag.setName(requestedTag.getName());
            productTag.setTagStatus(ProductTagStatus.REQUESTED);
            productTag.setRequestedByID(requestedTag.getRequestedByID());
            return productTagRepository.save(productTag);
        }
    }

    public List<ProductTag> getAllTags() {
        return productTagRepository.findAll();
    }

    public ProductTag approveTag(String tagName) {
        Optional<ProductTag> optionalProductTag = productTagRepository.findByName(tagName);
        if (optionalProductTag.isPresent()) {
            ProductTag productTag = optionalProductTag.get();
            productTag.setTagStatus(ProductTagStatus.APPROVED);
            return productTagRepository.save(productTag);
        } else {
            // Handle the case when the tag is not found. You could throw a custom exception or return null.
//            throw new IllegalStateException("Tag with name '" + tagName + "' not found.");
            throw new TagNotFoundException();
        }
    }
}
