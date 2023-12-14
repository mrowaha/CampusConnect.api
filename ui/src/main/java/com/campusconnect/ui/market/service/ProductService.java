package com.campusconnect.ui.market.service;

import com.campusconnect.domain.ProductTag.entity.ProductTag;
import com.campusconnect.domain.ProductTag.enums.ProductTagStatus;
import com.campusconnect.domain.ProductTag.repository.ProductTagRepository;
import com.campusconnect.domain.product.dto.ProductDto;
import com.campusconnect.domain.product.entity.Product;
import com.campusconnect.domain.product.enums.ProductStatus;
import com.campusconnect.domain.product.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductTagRepository productTagRepository;

    public ResponseEntity<?> saveProduct(ProductDto productCreationInfo){

        Product product = Product.builder()
                .sellerId(UUID.fromString(productCreationInfo.getSellerId()))
                .creationDate(LocalDate.now())
                .name(productCreationInfo.getName())
                .description(productCreationInfo.getDescription())
                .price(productCreationInfo.getPrice())
                .viewCount(0)
                .type(productCreationInfo.getType()) // for now
                .status(ProductStatus.AVAILABLE)
                .wishListedBy(new HashSet<UUID>())
                .bids(new ArrayList<UUID>())
                .tags(new HashSet<String>()).build();

        productRepository.save(product);
        return new ResponseEntity<>( "Product Id:" + product.getProductId(), HttpStatus.OK);
    }

    public List<Product> fetchProductList(){
        return (List<Product>)productRepository.findAll();
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
}
