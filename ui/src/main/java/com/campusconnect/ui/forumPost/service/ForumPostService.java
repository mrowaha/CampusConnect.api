package com.campusconnect.ui.forumPost.service;

import com.campusconnect.domain.forumPost.dto.ForumPostDto;
import com.campusconnect.domain.forumPost.entity.ForumPost;
import com.campusconnect.domain.forumPost.enums.ForumPostStatus;
import com.campusconnect.domain.forumPost.enums.ForumPostType;
import com.campusconnect.domain.forumPost.repository.ForumPostRepository;
import com.campusconnect.domain.user.entity.Bilkenteer;
import com.campusconnect.domain.user.repository.BilkenteerRepository;
import com.campusconnect.ui.forumPost.exceptions.ForumPostNotFound;
import com.campusconnect.ui.user.exceptions.UserNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
//import org.hibernate.search.engine.search.query.SearchResult;
//import org.hibernate.search.mapper.orm.Search;
//import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.stereotype.Service;

//import javax.persistence.PersistenceContexts;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Transactional
@Slf4j
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

    public List<ForumPost> searchLostForumPostList(String keywords){
        return forumPostRepository.findAllByKeywordsAndTypeAndStatus(keywords, ForumPostType.LOST, ForumPostStatus.UNRESOLVED).orElse(null);
    }

    public List<ForumPost> searchFoundForumPostList(String keywords){
        return forumPostRepository.findAllByKeywordsAndTypeAndStatus(keywords, ForumPostType.FOUND, ForumPostStatus.UNRESOLVED).orElse(null);
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
