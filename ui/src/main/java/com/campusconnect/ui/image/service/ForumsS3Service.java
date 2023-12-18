package com.campusconnect.ui.image.service;


import com.campusconnect.domain.forumPost.entity.ForumPost;
import com.campusconnect.domain.forumPost.repository.ForumPostRepository;
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
import org.springframework.http.HttpHeaders;
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
public class ForumsS3Service {

    private final ForumPostRepository forumPostRepository;
    private final MinioService minioService;
    private final ImageTypeUtils imageTypeUtils;

    public List<FileResponse> uploadBatch(Map<String, MultipartFile> files, String email, UUID forumId)
            throws InvalidFileTypeException, GenericMinIOFailureException, ProductNotFoundException, UnAuthorizedImageUpload
    {
        ForumPost forumPost = forumPostRepository.findById(forumId)
                .orElseThrow(ProductNotFoundException::new);
        if (!forumPost.getPostingUser().getEmail().equals(email)) {
            throw new UnAuthorizedImageUpload();
        }

        String saltedName =  forumId.toString().replaceAll("-", "");
        List<FileResponse> fileResponses = new ArrayList<>();
        ArrayList<String> oldImages = new ArrayList<>(forumPost.getImages());

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
        forumPost.setImages(oldImages);
        forumPostRepository.save(forumPost);
        return fileResponses;
    }


    public void get(HttpServletResponse response, UUID forumId)
            throws GenericMinIOFailureException, IOException {
        ForumPost forumPost = forumPostRepository.findById(forumId)
                .orElseThrow(ProductNotFoundException::new);

        String saltedName = forumId.toString().replaceAll("-", "");
        File zipFile = File.createTempFile("files", ".zip");

        try (FileOutputStream fos = new FileOutputStream(zipFile);
             ZipOutputStream zos = new ZipOutputStream(fos)) {

            List<String> fileNames = forumPost.getImages();

            for (String fileName : fileNames) {
                InputStream in = null;

                try {
                    String objectName = saltedName + fileName;
                    log.info("Fetching object: {}", objectName);

                    in = minioService.getProductPicture(objectName);
                    ZipEntry zipEntry = new ZipEntry(objectName);  // Use original file name in the zip entry
                    zos.putNextEntry(zipEntry);
                    IOUtils.copy(in, zos);
                    zos.closeEntry();  // Close the zip entry

                } catch (Exception e) {
                    log.error("Error processing image: {}", e.getMessage(), e);
                    throw new GenericMinIOFailureException();
                } finally {
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {
                            log.warn("Error closing InputStream: {}", e.getMessage(), e);
                        }
                    }
                }
            }
        } catch (IOException e) {
            log.error("Error creating zip file: {}", e.getMessage(), e);
            throw new GenericMinIOFailureException();
        }

        // Set the content type and headers in the HTTP response
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment;filename=" + zipFile.getName());

        // Copy the zip file to the response output stream
        try (InputStream zipInputStream = new FileInputStream(zipFile)) {
            IOUtils.copy(zipInputStream, response.getOutputStream());
        } finally {
            // Delete the temporary zip file
            if (!zipFile.delete()) {
                log.warn("Failed to delete temporary zip file: {}", zipFile.getAbsolutePath());
            }
        }
    }


}
