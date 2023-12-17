package com.campusconnect.ui.market.service;

import com.campusconnect.domain.ProductTag.entity.ProductTag;
import com.campusconnect.domain.ProductTag.enums.ProductTagStatus;
import com.campusconnect.domain.ProductTag.repository.ProductTagRepository;
import com.campusconnect.domain.forumPost.entity.ForumPost;
import com.campusconnect.domain.notification.dto.NotificationDto;
import com.campusconnect.domain.notification.entity.Notification;
import com.campusconnect.domain.notification.enums.NotificationType;
import com.campusconnect.domain.notification.repository.NotificationRepository;
import com.campusconnect.domain.product.dto.ProductDto;
import com.campusconnect.domain.product.dto.ProductSearchDto;
import com.campusconnect.domain.product.entity.Product;
import com.campusconnect.domain.product.enums.ProductStatus;
import com.campusconnect.domain.product.repository.ProductRepository;
import com.campusconnect.domain.user.entity.Bilkenteer;
import com.campusconnect.domain.user.entity.User;
import com.campusconnect.domain.user.repository.BilkenteerRepository;
import com.campusconnect.domain.transaction.entity.Bid;
import com.campusconnect.ui.market.exceptions.ProductNotFoundException;
import com.campusconnect.ui.user.exceptions.UserNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class ProductService {

    // Repositories for database operations
    private final ProductRepository productRepository;
    private final ProductTagRepository productTagRepository;
    private final BilkenteerRepository bilkenteerRepository;
    private final NotificationRepository notificationRepository;

    // EntityManager for JPA operations
    @Autowired
    private EntityManager entityManager;

    /**
     * Saves a new product to the database and notifies users subscribed to relevant tags.
     *
     * @param productCreationInfo Information about the product to be created.
     * @return ResponseEntity indicating success or failure along with the product ID.
     */
    public ResponseEntity<?> saveProduct(ProductDto productCreationInfo){
        Bilkenteer bilkenteer = bilkenteerRepository.findById(UUID.fromString(productCreationInfo.getSellerId()))
                .orElseThrow(UserNotFoundException::new);
        Set<String> tags = new HashSet<>(productCreationInfo.getTagNames());

        List<ProductTag> acceptedTags = productTagRepository.findAll();
        if (!tags.stream().allMatch(tag -> acceptedTags.stream().anyMatch(acceptedTag -> acceptedTag.getName().equals(tag)))) {
            return new ResponseEntity<>("Invalid tags. Some tags are not accepted.", HttpStatus.BAD_REQUEST);
        }

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
                .tags(acceptedTags.stream().map(ProductTag::getName).collect(Collectors.toSet())).build();

        productRepository.save(product);
        bilkenteer.getProducts().add(product);

        notifyUsersForTags(tags, product);

        return new ResponseEntity<>( "Product Id:" + product.getProductId(), HttpStatus.OK);
    }
    /**
     * Fetches a list of available products from the database.
     *
     * @return List of available products.
     */
    public List<Product> fetchProductList(){
        List<Product> allProducts = productRepository.findAll();

        List<Product> availableProducts = allProducts.stream()
                .filter(product -> product.getStatus() == ProductStatus.AVAILABLE)
                .collect(Collectors.toList());

        return availableProducts;
    }
    /**
     * Fetches a product by its ID, increments view count, and updates the database.
     *
     * @param productId ID of the product to fetch.
     * @return Product with the specified ID.
     */
    public Product fetchProductById(UUID productId){

        Product product = productRepository.findById(productId).orElse(null);

        if (Objects.nonNull(product)){
            product.setViewCount(product.getViewCount() + 1);

            productRepository.save(product);
        }

        return product;
    }
    /**
     * Updates an existing product based on the provided information.
     *
     * @param productCreationInfo Information about the product to be updated.
     * @param productId           ID of the product to update.
     * @return Updated product.
     */
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
    /**
     * Assigns a tag to a product if the tag is approved.
     *
     * @param tagName   Name of the tag to be assigned.
     * @param productId ID of the product.
     * @return Updated product.
     */
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
    /**
     * Deletes a product by its ID.
     *
     * @param productId ID of the product to delete.
     */
    public void deleteProductById(UUID productId){
        productRepository.deleteById(productId);
    }
    /**
     * Fetches products associated with a user by their ID.
     *
     * @param userId ID of the user.
     * @return List of products associated with the user.
     * @throws UserNotFoundException If the user is not found.
     */
    public List<Product> fetchProductsByUserId(UUID userId) throws UserNotFoundException {
        return productRepository.findAllBySellerUserId(userId).orElseThrow(UserNotFoundException::new);
    }
    /**
     * Retrieves bids associated with a product by its ID.
     *
     * @param productId ID of the product.
     * @return List of bids for the product.
     */
    public List<Bid> getProductBids(UUID productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException());

        return product.getBids();
    }
    /**
     * Searches for products based on the specified criteria.
     *
     * @param productSearchDto Search criteria.
     * @return List of products matching the criteria.
     */
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

        if (productSearchDto.getTags() != null && !productSearchDto.getTags().isEmpty()) {
            // Join the product with its tags and check if any of the tags in the request are present in the product
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
    /**
     * Notifies users subscribed to certain tags about a new product.
     *
     * @param tags    Set of tags associated with the product.
     * @param product Product for which notifications are generated.
     */
    private void notifyUsersForTags(Set<String> tags, Product product) {
        List<Bilkenteer> subscribedUsers = bilkenteerRepository.findBySubscribedTags_NameIn(tags);

        for (User user : subscribedUsers) {
            Notification notification = new Notification();
            notification.setUser(user);
            notification.setCreatedAt(LocalDateTime.now());
            notification.setSeen(false);
            notification.setType(NotificationType.PRODUCT);
            notification.setContent("New product posted with tags: " + String.join(", ", tags));

            // Save the notification to the repository
            notificationRepository.save(notification);
        }
    }


}

