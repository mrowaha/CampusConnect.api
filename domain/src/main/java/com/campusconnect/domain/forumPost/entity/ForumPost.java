package com.campusconnect.domain.forumPost.entity;

import com.campusconnect.domain.comment.entity.Comment;
import com.campusconnect.domain.forumPost.enums.ForumPostStatus;
import com.campusconnect.domain.forumPost.enums.ForumPostType;
import com.campusconnect.domain.user.entity.Bilkenteer;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@EntityListeners(ForumPostListener.class)
@Table(name = "cc_forum_post")
public class ForumPost {

    // Unique identifier for the forum post
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    protected UUID forumPostId;

    // User who created the forum post
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "posting_user_id")
    protected Bilkenteer postingUser;

    // Timestamp when the forum post was created
    @Column(name = "created_at", nullable = false)
    protected LocalDateTime createdAt;

    // Title of the forum post
    @Column(name = "title", nullable = false)
    @NotNull
    protected String title;

    // Description or content of the forum post
    @Column(name = "description", nullable = false)
    @NotNull
    protected String description;

    // Number of views the forum post has received
    @Column(name = "view_count", nullable = false)
    protected Integer viewCount = 0;

    // Type of the forum post (e.g., LOST, FOUND)
    @Column(name = "postType")
    @Enumerated(EnumType.STRING)
    protected ForumPostType postType;

    // Status of the forum post (e.g., unresolved, resolved)
    @Column(name = "postStatus")
    @Enumerated(EnumType.STRING)
    private ForumPostStatus postStatus = ForumPostStatus.UNRESOLVED;

    // Set of comments associated with the forum post
    @OneToMany(mappedBy = "forumPost", cascade = CascadeType.ALL)
    private Set<Comment> comments = new HashSet();
}
