package com.campusconnect.ui.image.service;

import com.campusconnect.domain.product.entity.Product;
import com.campusconnect.domain.product.repository.ProductRepository;
import com.campusconnect.domain.user.entity.User;
import com.campusconnect.domain.user.enums.Role;
import com.campusconnect.email.otp.OTPStrategy;
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

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductS3Service {

    private final ProductRepository productRepository;
    private final MinioService minioService;
    private final ImageTypeUtils imageTypeUtils;

    public List<FileResponse> uploadBatch(Map<String, MultipartFile> files, String email, UUID productId)
            throws InvalidFileTypeException, GenericMinIOFailureException, ProductNotFoundException, UnAuthorizedImageUpload
    {
        Product product = productRepository.findById(productId)
                .orElseThrow(ProductNotFoundException::new);
        if (!product.getSeller().getEmail().equals(email)) {
            throw new UnAuthorizedImageUpload();
        }

        String saltedName =  productId.toString().replaceAll("-", "");
        List<FileResponse> fileResponses = new ArrayList<>();
        ArrayList<String> oldImages = new ArrayList<>(product.getImages());

        ArrayList<MultipartFile> fileList = new ArrayList<>(files.values());
        for (int i = 0 ; i < files.size(); i++) {
            String fileType = null;
            try {
                log.info("file type");
                fileType = imageTypeUtils.getImageType(fileList.get(i));
                log.info("file type is {}", fileType);
            } catch (IOException e) {
                throw new GenericMinIOFailureException();
            }
            String random = OTPStrategy.generateRandomString(3);
            String fileTypeSuffix = fileType.substring(fileType.lastIndexOf("/")).replaceAll("/", ".");
            String objectName = saltedName + random + fileTypeSuffix;
            log.info("Object Name {}", objectName);
            oldImages.add(random+fileTypeSuffix);
            FileResponse response = minioService.putProductPicture(fileList.get(i), objectName);
            fileResponses.add(response);
        }
        product.setImages(oldImages);
        productRepository.save(product);
        return fileResponses;
    }

    public FileResponse uploadSingle(MultipartFile imageFile, String email, UUID productId)
            throws InvalidFileTypeException, GenericMinIOFailureException, ProductNotFoundException, UnAuthorizedImageUpload
    {
        Product product = productRepository.findById(productId)
                .orElseThrow(ProductNotFoundException::new);
        if (!product.getSeller().getEmail().equals(email)) {
            throw new UnAuthorizedImageUpload();
        }

        String saltedName =  productId.toString().replaceAll("-", "");
        log.info("Salted Name for email {} and product {} is {}", email, productId, saltedName);
        String fileType = null;
        try {
            fileType = imageTypeUtils.getImageType(imageFile);
        } catch (IOException e) {
            throw new GenericMinIOFailureException();
        }
        String random = OTPStrategy.generateRandomString(3);
        String fileTypeSuffix = fileType.substring(fileType.lastIndexOf("/")).replaceAll("/", ".");
        String objectName = saltedName + random + fileTypeSuffix;
        log.info("Object Name {}", objectName);
        product.getImages().add(random+fileTypeSuffix);

        FileResponse response = minioService.putProductPicture(imageFile, objectName);
        productRepository.save(product);
        return response;
    }

    public void get(HttpServletResponse response, UUID productId)
            throws GenericMinIOFailureException, IOException
    {
        Product product = productRepository.findById(productId)
                .orElseThrow(ProductNotFoundException::new);

        String saltedName =  productId.toString().replaceAll("-", "");
        File zipFile = File.createTempFile("files", ".zip");
        try (FileOutputStream fos = new FileOutputStream(zipFile);
             ZipOutputStream zos = new ZipOutputStream(fos)) {
            List<String> fileNames = product.getImages();
            for (String fileName : fileNames) {
                InputStream in = null;
                try {
                    String objectName = saltedName + fileName;
                    log.info("fetch object {}", objectName);
                    in = minioService.getProductPicture(objectName);
                    ZipEntry zipEntry = new ZipEntry(objectName);
                    zos.putNextEntry(zipEntry);
                    IOUtils.copy(in, zos);

                } catch (Exception e) {
                    throw new GenericMinIOFailureException();
                } finally {
                    if (in != null) in.close();
                }
            }

            response.setContentType("application/zip");
            response.setHeader("Content-Disposition", "attachment;filename=files.zip");
            try (InputStream zipInputStream = new FileInputStream(zipFile)) {
                IOUtils.copy(zipInputStream, response.getOutputStream());
            }
        } finally {
            // Delete the temporary zip file
            zipFile.delete();
        }
    }


}
