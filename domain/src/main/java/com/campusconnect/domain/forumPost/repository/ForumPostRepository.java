package com.campusconnect.domain.forumPost.repository;

import com.campusconnect.domain.forumPost.entity.ForumPost;
import com.campusconnect.domain.forumPost.enums.ForumPostStatus;
import com.campusconnect.domain.forumPost.enums.ForumPostType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ForumPostRepository extends JpaRepository<ForumPost, UUID> {

    Optional<List<ForumPost>> findAllByPostingUserUserId(UUID userId);

    Optional<List<ForumPost>> findAllByPostTypeAndPostStatus(ForumPostType postType, ForumPostStatus postStatus);

    @Query("SELECT fp FROM ForumPost fp "
            + "WHERE (LOWER(fp.title) LIKE LOWER(CONCAT('%', :keywords, '%')) " +
            "OR LOWER(fp.description) LIKE LOWER(CONCAT('%', :keywords, '%'))) " +
            "AND fp.postType = :postType AND fp.postStatus = :postStatus")
    Optional<List<ForumPost>> findAllByKeywordsAndTypeAndStatus(String keywords, ForumPostType postType, ForumPostStatus postStatus);
}
