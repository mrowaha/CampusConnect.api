package com.campusconnect.domain.comment.entity;

import com.campusconnect.domain.forumPost.entity.ForumPost;
import com.campusconnect.domain.messageThread.entity.MessageThread;
import com.campusconnect.domain.product.entity.Product;
import com.campusconnect.domain.user.entity.Bilkenteer;
import com.campusconnect.domain.user.entity.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@RequiredArgsConstructor
@Table(name = "cc_comment")
public class Comment{

    // Unique identifier for the comment
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    protected UUID id;

    // Forum post to which the comment belongs
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "forum_post_id")
    @JsonBackReference(value = "comments")
    @NonNull
    private ForumPost forumPost;

    // Timestamp when the comment was created
    @Column(name = "timeStamp", nullable = false)
    @NonNull
    private LocalDateTime timeStamp;

    // Content or text of the comment
    @Column(name = "content", nullable = false)
    @NonNull
    private String content;

    // Bilkenteer who posted the comment
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "commenter_Id")
    private Bilkenteer commenter;

//    @Transient
//    private UUID comm;
//
//    @PostLoad
//    private void fillTransientFields() {
//        if (sender != null) {
//            this.senderId = sender.getUserId();
//        }
//        if (receiver != null) {
//            this.receiverId = receiver.getUserId();
//        }
//    }
}
