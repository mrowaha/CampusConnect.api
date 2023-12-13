package com.campusconnect.ui.market.service;

import com.campusconnect.ui.market.dto.ProductDto;
import com.campusconnect.ui.market.entity.Product;
import com.campusconnect.ui.market.enums.ProductStatus;
import com.campusconnect.ui.market.enums.ProductType;
import com.campusconnect.ui.market.repository.ProductRepository;
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
                .bids(new ArrayList<Integer>())
                .categorizesId(new HashSet<Integer>()).build();

        productRepository.save(product);
        return new ResponseEntity<>( "Product Id:" + product.getProductId(), HttpStatus.OK);
    }

    public List<Product> fetchProductList(){
        return (List<Product>)productRepository.findAll();
    }

    public Product updateProduct(ProductDto productCreationInfo, Long productId){
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

    public void deleteProductById(Long productId){
        productRepository.deleteById(productId);
    }
}
