package com.campusconnect.ui.forumPost.service;

import com.campusconnect.domain.forumPost.dto.ForumPostDto;
import com.campusconnect.domain.forumPost.entity.ForumPost;
import com.campusconnect.domain.forumPost.enums.ForumPostStatus;
import com.campusconnect.domain.forumPost.enums.ForumPostType;
import com.campusconnect.domain.forumPost.repository.ForumPostRepository;
import com.campusconnect.domain.notification.dto.NotificationDto;
import com.campusconnect.domain.notification.entity.Notification;
import com.campusconnect.domain.product.dto.ProductDto;
import com.campusconnect.domain.product.entity.Product;
import com.campusconnect.domain.product.enums.ProductStatus;
import com.campusconnect.domain.product.repository.ProductRepository;
import com.campusconnect.domain.user.entity.Bilkenteer;
import com.campusconnect.domain.user.entity.User;
import com.campusconnect.domain.user.repository.BilkenteerRepository;
import com.campusconnect.ui.forumPost.exceptions.ForumPostNotFound;
import com.campusconnect.ui.user.exceptions.UserNotFoundException;
import com.campusconnect.ui.user.service.BilkenteerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class ForumPostService {
    private final ForumPostRepository forumPostRepository;
    private final BilkenteerRepository bilkenteerRepository;

    public List<ForumPost> fetchUserForumPostList(UUID userId){
        return forumPostRepository.findAllByPostingUserUserId(userId).orElse(null);
    }

    public List<ForumPost> fetchLostForumPostList(){
        return forumPostRepository.findAllByPostTypeAndPostStatus(ForumPostType.LOST, ForumPostStatus.UNRESOLVED).orElse(null);
    }

    public List<ForumPost> fetchFoundForumPostList(){
        return forumPostRepository.findAllByPostTypeAndPostStatus(ForumPostType.FOUND, ForumPostStatus.UNRESOLVED).orElse(null);
    }

    public ForumPost saveForumPost(UUID userId, ForumPostDto forumPostDto) throws UserNotFoundException {

        ForumPost forumPost = new ForumPost();

        Bilkenteer bilkenteer = bilkenteerRepository.findById(userId).orElse(null);
        if (Objects.isNull(bilkenteer)) {
            throw new UserNotFoundException();
        }

        forumPost.setPostingUser(bilkenteer);
        forumPost.setTitle(forumPostDto.getTitle());
        forumPost.setCreatedAt(LocalDateTime.now());
        forumPost.setPostType(forumPostDto.getPostType());
        forumPost.setDescription(forumPostDto.getDescription());
        forumPost.setViewCount(0);
        forumPost.setPostStatus(ForumPostStatus.UNRESOLVED);

        forumPostRepository.save(forumPost);

        return forumPost;
    }

    public ForumPost updateForumPost(ForumPostDto forumPostDto, UUID ForumPostId){
        ForumPost forumPost = forumPostRepository.findById(ForumPostId).orElseThrow(ForumPostNotFound::new);


        if (Objects.nonNull(forumPostDto.getTitle())){
            forumPost.setTitle(forumPostDto.getTitle());
        }

        if (Objects.nonNull(forumPostDto.getTitle())){
            forumPost.setDescription(forumPostDto.getDescription());
        }

        if (forumPostDto.getPostStatus().equals(ForumPostStatus.RESOLVED)){
            forumPost.setPostStatus(ForumPostStatus.RESOLVED);
        }

        return forumPostRepository.save(forumPost);
    }

    public void deleteForumPostById(UUID ForumPostId){
        forumPostRepository.deleteById(ForumPostId);
    }
}
