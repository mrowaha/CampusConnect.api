package com.campusconnect.domain.ProductTag.repository;

import com.campusconnect.domain.ProductTag.entity.ProductTag;
import com.campusconnect.domain.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductTagRepository extends JpaRepository<ProductTag, UUID> {

    @Query("SELECT t from ProductTag t WHERE t.name = :tagName")
    Optional<ProductTag> findByName(@Param("tagName") String tagName);


    @Query("SELECT p FROM Product p INNER JOIN p.tagsId t WHERE t = :tagName")
    List<Product> findProducts(@Param("tagName") String tagName);

//     addProduct(tagName, productId)
//     Optional<ProductTag> insertTag(String tagName);
//     findSubscribers(tagName)
//     subscribe(tagName)
//     Optional<ProductTag> acceptTag(String tagName);
//     Optional<ProductTag> findByName(String name);


//    @Query("SELECT s FROM Subscriber s INNER JOIN s.subscriptions t WHERE t.name = :tagName")
//    List<Subscriber> findSubscribers(@Param("tagName") String tagName);
//
//    @Query("SELECT t FROM ProductTag t WHERE :subscriber MEMBER OF t.subscribers")
//    List<ProductTag> findTagsBySubscriber(@Param("subscriber") Subscriber subscriber);
}