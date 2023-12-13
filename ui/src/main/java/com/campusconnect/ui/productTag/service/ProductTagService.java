package com.campusconnect.ui.productTag.service;



import com.campusconnect.domain.ProductTag.dto.ProductTagDto;
import com.campusconnect.domain.ProductTag.entity.ProductTag;
import com.campusconnect.domain.ProductTag.enums.ProductTagStatus;
import com.campusconnect.domain.ProductTag.repository.ProductTagRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductTagService {
    private final ProductTagRepository productTagRepository;

    public ProductTag requestProductTag(String tagName){
//        // Implement the logic to request a new product tag
//        // This is a simplistic example. You'll need to add your business logic here.
//        ProductTag productTag = new ProductTag();
//        productTag.setName(tagName);
//        productTag.setTagStatus(ProductTagStatus.REQUESTED);
//        return productTagRepository.save(productTag);

        System.out.println("Trying to request tag");
        return null;
    }

    public List<ProductTag> getAllTags(){
//        return productTagRepository.findAll();
        System.out.println("Trying to find all tags");
        return null;
    }

    public ProductTag approveTag(String tagName){
//        // Implement the logic to approve a tag
//        ProductTag productTag = productTagRepository.findByName(tagName);
//        if(productTag != null){
//            productTag.setTagStatus(ProductTagStatus.APPROVED);
//            return productTagRepository.save(productTag);
//        }
//        return null; // Or throw an exception if preferred
        System.out.println("Trying to approve tag");
        return null;
    }
}