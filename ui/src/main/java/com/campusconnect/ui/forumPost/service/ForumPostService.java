package com.campusconnect.ui.forumPost.service;

import com.campusconnect.domain.comment.dto.CommentDto;
import com.campusconnect.domain.comment.entity.Comment;
import com.campusconnect.domain.comment.repository.CommentRepository;
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
import jakarta.persistence.Id;
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

    // Repositories for database operations
    private final ForumPostRepository forumPostRepository;
    private final BilkenteerRepository bilkenteerRepository;
    private final CommentRepository commentRepository;

    /**
     * Fetches a list of forum posts created by a user.
     *
     * @param userId ID of the user.
     * @return List of forum posts created by the user.
     */
    public List<ForumPost> fetchUserForumPostList(UUID userId){
        return forumPostRepository.findAllByPostingUserUserId(userId).orElse(null);
    }

    /**
     * Fetches a forum post by its ID.
     *
     * @param forumPostId ID of the forum post.
     * @return The forum post with the specified ID.
     */
    public ForumPost fetchForumPostById(UUID forumPostId){
        return forumPostRepository.findById(forumPostId).orElse(null);
    }

    /**
     * Fetches a list of unresolved lost forum posts.
     *
     * @return List of unresolved lost forum posts.
     */
    public List<ForumPost> fetchLostForumPostList(){
        return forumPostRepository.findAllByPostTypeAndPostStatus(ForumPostType.LOST, ForumPostStatus.UNRESOLVED).orElse(null);
    }

    /**
     * Fetches a list of unresolved found forum posts.
     *
     * @return List of unresolved found forum posts.
     */
    public List<ForumPost> fetchFoundForumPostList(){
        return forumPostRepository.findAllByPostTypeAndPostStatus(ForumPostType.FOUND, ForumPostStatus.UNRESOLVED).orElse(null);
    }

    /**
     * Searches for unresolved lost forum posts based on keywords.
     *
     * @param keywords Keywords for searching.
     * @return List of unresolved lost forum posts matching the keywords.
     */
    public List<ForumPost> searchLostForumPostList(String keywords){
        return forumPostRepository.findAllByKeywordsAndTypeAndStatus(keywords, ForumPostType.LOST, ForumPostStatus.UNRESOLVED).orElse(null);
    }

    /**
     * Searches for unresolved found forum posts based on keywords.
     *
     * @param keywords Keywords for searching.
     * @return List of unresolved found forum posts matching the keywords.
     */
    public List<ForumPost> searchFoundForumPostList(String keywords){
        return forumPostRepository.findAllByKeywordsAndTypeAndStatus(keywords, ForumPostType.FOUND, ForumPostStatus.UNRESOLVED).orElse(null);
    }

    /**
     * Adds a comment to a forum post.
     *
     * @param userId      ID of the commenting user.
     * @param commentDto  Comment data.
     * @return The created comment.
     * @throws UserNotFoundException  If the commenting user is not found.
     * @throws ForumPostNotFound      If the forum post for commenting is not found.
     */
    public Comment commentOnForumPost(UUID userId, CommentDto commentDto) throws UserNotFoundException {

        Comment comment = new Comment();

        // Check for Bilkenteer Exists
        Bilkenteer bilkenteer = bilkenteerRepository.findById(userId).orElse(null);
        if (Objects.isNull(bilkenteer)) {
            throw new UserNotFoundException();
        }

        // Check for ForumPost Exists
        ForumPost forumPost = forumPostRepository.findById(commentDto.getForumPostId()).orElse(null);
        if (Objects.isNull(forumPost)) {
            throw new ForumPostNotFound();
        }

        comment.setCommenter(bilkenteer);
        comment.setContent(commentDto.getContent());
        comment.setForumPost(forumPost);
        comment.setTimeStamp(LocalDateTime.now());

        commentRepository.save(comment);

        return comment;
    }

    /**
     * Saves a new forum post.
     *
     * @param userId        ID of the user creating the forum post.
     * @param forumPostDto  Forum post data.
     * @return The created forum post.
     * @throws UserNotFoundException  If the user creating the forum post is not found.
     */
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

    /**
     * Updates an existing forum post.
     *
     * @param forumPostDto  Updated forum post data.
     * @param ForumPostId   ID of the forum post to be updated.
     * @return The updated forum post.
     * @throws ForumPostNotFound  If the forum post to be updated is not found.
     */
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

    /**
     * Deletes a forum post by its ID.
     *
     * @param ForumPostId ID of the forum post to be deleted.
     */
    public void deleteForumPostById(UUID ForumPostId){
        forumPostRepository.deleteById(ForumPostId);
    }
}
