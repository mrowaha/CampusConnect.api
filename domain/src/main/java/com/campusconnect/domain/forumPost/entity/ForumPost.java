package com.campusconnect.domain.forumPost.entity;

import com.campusconnect.domain.forumPost.enums.ForumPostStatus;
import com.campusconnect.domain.forumPost.enums.ForumPostType;
import com.campusconnect.domain.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "cc_forum_post")
public class ForumPost {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    protected UUID forumPostId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "posting_user_id", nullable = false)
    protected User postingUser;

    @Column(name = "created_at", nullable = false)
    protected LocalDateTime createdAt;

    @Column(name = "title", nullable = false)
    @NotNull
    protected String title;

    @Column(name = "description", nullable = false)
    @NotNull
    protected String description;

    @Column(name = "view_count", nullable = false)
    protected Integer viewCount = 0;


    @Column(name = "postType")
    @Enumerated(EnumType.STRING)
    protected ForumPostType postType;

    @Column(name = "postStatus")
    @Enumerated(EnumType.STRING)
    private ForumPostStatus postStatus = ForumPostStatus.Unresolved;

}
