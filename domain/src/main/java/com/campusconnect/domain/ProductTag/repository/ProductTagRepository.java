package com.campusconnect.domain.ProductTag.repository;

import com.campusconnect.domain.ProductTag.entity.ProductTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductTagRepository extends JpaRepository<ProductTag, UUID> {

    // findProducts(tagName)
    // addProduct(tagName, productId)
    // insertTag(tagName)
    // findSubscribers(tagName)
    // subscribe(tagName)
    // acceptTag(tagName)
    List<ProductTag> findByName(String name);
    // Other custom queries can be added here based on requirements.
}