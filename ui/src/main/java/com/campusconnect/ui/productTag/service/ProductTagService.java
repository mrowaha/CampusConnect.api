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
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductTagService {
    private final ProductTagRepository productTagRepository;

    /**
     * Requests a new product tag.
     *
     * @param requestedTag Information about the requested tag.
     * @param requesterID user id of the requesting entity
     * @return The newly created product tag.
     * @throws TagAlreadyExistsException If the tag with the same name already exists.
     */
    public ProductTag requestProductTag(ProductTagDto requestedTag, UUID requesterID) throws TagAlreadyExistsException {
        Optional<ProductTag> existingTag = productTagRepository.findByName(requestedTag.getName());
        if (existingTag.isPresent()) {
            throw new TagAlreadyExistsException();
        }
        ProductTag productTag = new ProductTag();
        productTag.setName(requestedTag.getName());
        productTag.setTagStatus(ProductTagStatus.REQUESTED);
        productTag.setRequestedByID(requesterID);
        return productTagRepository.save(productTag);
    }
    /**
     * Retrieves all product tags from the database.
     *
     * @return List of all product tags.
     */
    public List<ProductTag> getAllTags() {
        return productTagRepository.findAll();
    }

    /**
     * Retrieves all requested product tags from the database.
     *
     * @return List of requested product tags.
     */
    public List<ProductTag> getRequestedTags() {
        return productTagRepository.getRequestedTags();
    }

    /**
     * Retrieves all approved product tags from the database.
     *
     * @return List of approved product tags.
     */
    public List<ProductTag> getApprovedTags() {
        return productTagRepository.getApprovedTags();
    }

    /**
     * Retrieves a product tag by its name.
     *
     * @param tagName Name of the product tag.
     * @return The product tag with the specified name.
     * @throws TagNotFoundException If the tag with the specified name is not found.
     */
    public ProductTag getProductTag(String tagName) throws TagNotFoundException {
        return productTagRepository.findByName(tagName).orElseThrow(TagNotFoundException::new);
    }

    /**
     * Approves a product tag by changing its status to APPROVED.
     *
     * @param tagName Name of the tag to be approved.
     * @return The approved product tag.
     * @throws TagNotFoundException If the tag with the specified name is not found.
     */
    public ProductTag approveTag(String tagName, UUID approverId) throws TagNotFoundException {
        Optional<ProductTag> optionalProductTag = productTagRepository.findByName(tagName);
        if (optionalProductTag.isPresent()) {
            ProductTag productTag = optionalProductTag.get();
            productTag.setTagStatus(ProductTagStatus.APPROVED);
            productTag.setAcceptedByID(approverId);
            return productTagRepository.save(productTag);
        } else {
            throw new TagNotFoundException();
        }
    }

    public List<ProductTag> batchUploadTags(List<String> tags, UUID uploadedBy) {
        List<ProductTag> tagEntites = tags.stream().map((tag) -> {
            return ProductTag.builder()
                    .tagStatus(ProductTagStatus.APPROVED)
                    .acceptedByID(uploadedBy)
                    .name(tag)
                    .build();
        }).collect(Collectors.toList());
        productTagRepository.saveAll(tagEntites);
        return tagEntites;
    }

}
