package com.campusconnect.ui.image.service;

import com.campusconnect.domain.product.entity.Product;
import com.campusconnect.domain.product.repository.ProductRepository;
import com.campusconnect.domain.user.entity.User;
import com.campusconnect.domain.user.enums.Role;
import com.campusconnect.image.dto.FileResponse;
import com.campusconnect.image.exceptions.GenericMinIOFailureException;
import com.campusconnect.image.exceptions.InvalidFileTypeException;
import com.campusconnect.image.service.MinioService;
import com.campusconnect.image.util.ImageTypeUtils;
import com.campusconnect.ui.image.exceptions.UnAuthorizedImageUpload;
import com.campusconnect.ui.market.exceptions.ProductNotFoundException;
import com.campusconnect.ui.user.exceptions.UserNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductS3Service {

    private final ProductRepository productRepository;
    private final MinioService minioService;
    private final ImageTypeUtils imageTypeUtils;

    public FileResponse uploadSingle(MultipartFile imageFile, String email, UUID productId)
            throws InvalidFileTypeException, GenericMinIOFailureException
    {
        Product product = productRepository.findById(productId)
                .orElseThrow(ProductNotFoundException::new);
        if (!product.getSeller().getEmail().equals(email)) {
            throw new UnAuthorizedImageUpload();
        }

        String base = email + "-" + productId;
        final MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new GenericMinIOFailureException();
        }
        final byte[] hash = digest.digest(base.getBytes(StandardCharsets.UTF_8));
        final StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            final String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1)
                hexString.append('0');
            hexString.append(hex);
        }
        String saltedName =  hexString.toString();

        log.info("Salted Name for email {} and product {} is {}", email, productId, saltedName);
        String fileType = null;
        try {
            fileType = imageTypeUtils.getImageType(imageFile);
        } catch (IOException e) {
            throw new GenericMinIOFailureException();
        }
        String objectName = saltedName + fileType.substring(fileType.lastIndexOf("/")).replaceAll("/", ".");
        log.info("Object Name {}", objectName);
        product.getImages().add(objectName);

        FileResponse response = minioService.putProfilePicture(imageFile, objectName);
        productRepository.save(product);
        return response;
    }


}
