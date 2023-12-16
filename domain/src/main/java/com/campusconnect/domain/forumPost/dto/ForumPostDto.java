package com.campusconnect.domain.forumPost.dto;

import com.campusconnect.domain.forumPost.enums.ForumPostStatus;
import com.campusconnect.domain.forumPost.enums.ForumPostType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ForumPostDto {

    private String posterId;

//    @NotBlank(message = "Post Title Can not be Blank")
//    @NotNull(message = "Post Title Can not be Null")
    private String title;

//    @NotBlank(message = "Description Can not be Blank")
//    @NotNull(message = "Description Can not be Null")
    private String description;

//    @NotBlank(message = "postType Can not be Blank")
//    @NotNull(message = "postType Can not be Null")
    private ForumPostType postType;

    private ForumPostStatus postStatus;

}
