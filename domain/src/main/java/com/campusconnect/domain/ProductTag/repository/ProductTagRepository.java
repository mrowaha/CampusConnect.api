package com.campusconnect.domain.ProductTag.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.campusconnect.domain.ProductTag.entity.ProductTag;
import com.campusconnect.domain.product.entity.Product;

@Repository
public interface ProductTagRepository extends JpaRepository<ProductTag, UUID> {

    @Query("SELECT t from ProductTag t WHERE t.name = :tagName")
    Optional<ProductTag> findByName(@Param("tagName") String tagName);

    @Query("SELECT p FROM Product p INNER JOIN p.tags t WHERE t = :tagName")
    List<Product> findProducts(@Param("tagName") String tagName);

    @Query("SELECT t from ProductTag t WHERE t.tagStatus = 'APPROVED'")
    List<ProductTag> getApprovedTags();

    @Query("SELECT t from ProductTag t WHERE t.tagStatus = 'REQUESTED'")
    List<ProductTag> getRequestedTags();
}