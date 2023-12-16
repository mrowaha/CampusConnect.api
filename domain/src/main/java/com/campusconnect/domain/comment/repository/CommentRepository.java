package com.campusconnect.domain.comment.repository;

import com.campusconnect.domain.comment.entity.Comment;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CommentRepository extends ListCrudRepository<Comment, UUID> {

    Optional<List<Comment>> findAllByForumPostForumPostId(UUID forumPostI);

}
