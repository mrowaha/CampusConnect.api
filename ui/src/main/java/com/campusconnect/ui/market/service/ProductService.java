package com.campusconnect.ui.market.service;

import com.campusconnect.domain.product.dto.ProductDto;
import com.campusconnect.domain.product.entity.Product;
import com.campusconnect.domain.product.enums.ProductStatus;
import com.campusconnect.domain.product.repository.ProductRepository;
import com.campusconnect.domain.user.entity.Bilkenteer;
import com.campusconnect.domain.user.repository.BilkenteerRepository;
import com.campusconnect.ui.user.exceptions.UserNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.campusconnect.ui.user.exceptions.UserNotFoundException;

import java.time.LocalDate;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final BilkenteerRepository bilkenteerRepository;

    public ResponseEntity<?> saveProduct(ProductDto productCreationInfo){
        Bilkenteer bilkenteer = bilkenteerRepository.findById(UUID.fromString(productCreationInfo.getSellerId()))
                .orElseThrow(UserNotFoundException::new);

        Product product = Product.builder()
                .sellerId(UUID.fromString(productCreationInfo.getSellerId()))
                .bilkenteer(bilkenteer)
                .creationDate(LocalDate.now())
                .name(productCreationInfo.getName())
                .description(productCreationInfo.getDescription())
                .price(productCreationInfo.getPrice())
                .viewCount(0)
                .type(productCreationInfo.getType()) // for now
                .status(ProductStatus.AVAILABLE)
                .wishListedBy(new HashSet<UUID>())
                .bids(new ArrayList<UUID>())
                .tagsId(new HashSet<UUID>()).build();

        productRepository.save(product);
        bilkenteer.getProducts().add(product);
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

    public void deleteProductById(UUID productId){
        productRepository.deleteById(productId);
    }

    public List<Product> fetchProductsByUserId(UUID userId) throws UserNotFoundException {
        Bilkenteer bilkenteer = bilkenteerRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        return bilkenteer.getProducts();
    }
}
