package com.campusconnect.domain.comment.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDto {

    private UUID id;

    @NonNull
    private UUID forumPostId;

    private LocalDateTime timestamp;

    @NonNull
    private String content;

    private UUID commenterId;

}