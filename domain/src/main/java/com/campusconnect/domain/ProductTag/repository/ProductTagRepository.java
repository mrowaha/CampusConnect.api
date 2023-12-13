package com.campusconnect.domain.ProductTag.repository;

import com.campusconnect.domain.ProductTag.entity.ProductTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductTagRepository extends JpaRepository<ProductTag, UUID> {

    // findProducts(tagName)
    // addProduct(tagName, productId)
    // insertTag(tagName)
    // findSubscribers(tagName)
    // subscribe(tagName)
    // acceptTag(tagName)

//    Optional<ProductTag> findByName(String name);

//    @Query("SELECT p FROM product p INNER JOIN p.tags t WHERE t.name = :tagName")
//    List<Product> findProducts(@Param("tagName") String tagName);
//
//    @Query("SELECT s FROM Subscriber s INNER JOIN s.subscriptions t WHERE t.name = :tagName")
//    List<Subscriber> findSubscribers(@Param("tagName") String tagName);
//
//    @Query("SELECT t FROM ProductTag t WHERE :subscriber MEMBER OF t.subscribers")
//    List<ProductTag> findTagsBySubscriber(@Param("subscriber") Subscriber subscriber);
}