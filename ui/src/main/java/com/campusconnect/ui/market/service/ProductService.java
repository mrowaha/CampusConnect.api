package com.campusconnect.ui.market.service;

import com.campusconnect.domain.ProductTag.entity.ProductTag;
import com.campusconnect.domain.ProductTag.enums.ProductTagStatus;
import com.campusconnect.domain.ProductTag.repository.ProductTagRepository;
import com.campusconnect.domain.forumPost.entity.ForumPost;
import com.campusconnect.domain.product.dto.ProductDto;
import com.campusconnect.domain.product.dto.ProductSearchDto;
import com.campusconnect.domain.product.entity.Product;
import com.campusconnect.domain.product.enums.ProductStatus;
import com.campusconnect.domain.product.repository.ProductRepository;
import com.campusconnect.domain.user.entity.Bilkenteer;
import com.campusconnect.domain.user.repository.BilkenteerRepository;
import com.campusconnect.domain.transaction.entity.Bid;
import com.campusconnect.ui.user.exceptions.UserNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductTagRepository productTagRepository;
    private final BilkenteerRepository bilkenteerRepository;

    @Autowired
    private EntityManager entityManager;

    public ResponseEntity<?> saveProduct(ProductDto productCreationInfo){
        Bilkenteer bilkenteer = bilkenteerRepository.findById(UUID.fromString(productCreationInfo.getSellerId()))
                .orElseThrow(UserNotFoundException::new);

        Product product = Product.builder()
                .seller(bilkenteer)
                .creationDate(LocalDate.now())
                .name(productCreationInfo.getName())
                .description(productCreationInfo.getDescription())
                .price(productCreationInfo.getPrice())
                .viewCount(0)
                .type(productCreationInfo.getType()) // for now
                .status(ProductStatus.AVAILABLE)
                .wishListedBy(new HashSet<UUID>())
                .bids(new ArrayList<Bid>())
                .tags(new HashSet<String>()).build();

        productRepository.save(product);
        return new ResponseEntity<>( "Product Id:" + product.getProductId(), HttpStatus.OK);
    }

    public List<Product> fetchProductList(){
        List<Product> allProducts = productRepository.findAll();

        List<Product> availableProducts = allProducts.stream()
                .filter(product -> product.getStatus() == ProductStatus.AVAILABLE)
                .collect(Collectors.toList());

        return availableProducts;
    }

    public Product fetchProductById(UUID productId){

        Product product = productRepository.findById(productId).orElse(null);

        if (Objects.nonNull(product)){
            product.setViewCount(product.getViewCount() + 1);

            productRepository.save(product);
        }

        return product;
    }

    public Product updateProduct(ProductDto productCreationInfo, UUID productId){
        Product productDB = productRepository.findById(productId).get();

        if (Objects.nonNull(productCreationInfo.getName()) && !"".equalsIgnoreCase(productCreationInfo.getName())){
            productDB.setName(productCreationInfo.getName());
        }

        if (Objects.nonNull(productCreationInfo.getDescription()) && !"".equalsIgnoreCase(productCreationInfo.getDescription())){
            productDB.setDescription(productCreationInfo.getDescription());
        }

        if (Objects.nonNull(productCreationInfo.getPrice())){
            productDB.setPrice(productCreationInfo.getPrice());
        }

        if (Objects.nonNull(productCreationInfo.getProductStatus())){
            productDB.setStatus(productCreationInfo.getProductStatus());
        }

        return productRepository.save(productDB);
    }

    public Product assignTag(String tagName, UUID productId){
        Product product = productRepository.findById(productId).get();
        ProductTag tag = productTagRepository.findByName(tagName).get();

        if (tag.getTagStatus() == ProductTagStatus.APPROVED) {
            Set<String> existingTags = product.getTags();
            existingTags.add(tag.getName());
            product.setTags(existingTags);
        } else {
            throw new IllegalStateException("Cannot assign unapproved tag");
        }
        return product;
    }

    public void deleteProductById(UUID productId){
        productRepository.deleteById(productId);
    }

    public List<Product> fetchProductsByUserId(UUID userId) throws UserNotFoundException {
        return productRepository.findAllBySellerUserId(userId).orElseThrow(UserNotFoundException::new);
    }

    public List<Product> searchProduct(ProductSearchDto productSearchDto) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> cq = cb.createQuery(Product.class);

        Root<Product> product = cq.from(Product.class);
        List<Predicate> predicates = new ArrayList<>();

        //Keyword can Match title or Description
        if (productSearchDto.getKeywords() != null) {
            String likePattern = "%" + productSearchDto.getKeywords().toLowerCase() + "%";
            Predicate namePredicate = cb.like(cb.lower(product.get("name")), likePattern);
            Predicate descriptionPredicate = cb.like(cb.lower(product.get("description")), likePattern);
            predicates.add(cb.or(namePredicate, descriptionPredicate));
        }

        //Tags must be present
        if (productSearchDto.getTags() != null && !productSearchDto.getTags().isEmpty()) {
            predicates.add(product.join("tags").in(productSearchDto.getTags()));
        }

        if (productSearchDto.getMinPrice() != null) {
            predicates.add(cb.greaterThanOrEqualTo(product.get("price"), productSearchDto.getMinPrice()));
        }

        if (productSearchDto.getMaxPrice() != null) {
            predicates.add(cb.lessThanOrEqualTo(product.get("price"), productSearchDto.getMaxPrice()));
        }

        //Minimum Trust Score
        if (productSearchDto.getSellerRating() != null) {
            predicates.add(cb.greaterThanOrEqualTo(product.get("seller").get("trustScore"), productSearchDto.getSellerRating()));
        }

        cq.where(predicates.toArray(new Predicate[0]));

        //Sorting Logic
        if (productSearchDto.getSortBy() != null) {
            switch (productSearchDto.getSortBy()) {
                case PRICE_ASCENDING:
                    cq.orderBy(cb.asc(product.get("price")));
                    break;
                case PRICE_DESCENDING:
                    cq.orderBy(cb.desc(product.get("price")));
                    break;
                case LATEST:
                    cq.orderBy(cb.desc(product.get("creationDate")));
                    break;
                case TRENDING:
                    cq.orderBy(cb.desc(product.get("viewCount")));
                    break;
            }
        }

        //Run native query
        TypedQuery<Product> query = entityManager.createQuery(cq);
        return query.getResultList();
    }

}
