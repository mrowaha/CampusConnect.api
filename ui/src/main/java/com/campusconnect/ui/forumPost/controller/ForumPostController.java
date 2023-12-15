package com.campusconnect.ui.forumPost.controller;

import com.campusconnect.domain.forumPost.dto.ForumPostDto;
import com.campusconnect.domain.forumPost.entity.ForumPost;
import com.campusconnect.domain.notification.entity.Notification;
import com.campusconnect.domain.security.RequiredScope;
import com.campusconnect.domain.security.SecurityScope;
import com.campusconnect.ui.common.controller.SecureController;
import com.campusconnect.ui.forumPost.service.ForumPostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/forumPosts")
public class ForumPostController extends SecureController {

    private final ForumPostService forumPostService;

    @PostMapping("/user/")
    @RequiredScope(scope = SecurityScope.NONE)
    public ResponseEntity<?> saveForumPost(@RequestParam("userId") UUID userId,@Valid @RequestBody ForumPostDto forumPostDto) {
        return ResponseEntity.ok(forumPostService.saveForumPost(userId, forumPostDto));
    }


    @GetMapping("/user/")
    @RequiredScope(scope = SecurityScope.NONE)
    public ResponseEntity<List<ForumPost>> fetchUserForumPostList(@RequestParam("userId") UUID userId){
        return ResponseEntity.ok(forumPostService.fetchUserForumPostList(userId));
    }

    @GetMapping("/lostForum")
    @RequiredScope(scope = SecurityScope.NONE)
    public ResponseEntity<List<ForumPost>> fetchLostForumPostList(){
        return ResponseEntity.ok(forumPostService.fetchLostForumPostList());
    }

    @GetMapping("/foundForum")
    @RequiredScope(scope = SecurityScope.NONE)
    public ResponseEntity<List<ForumPost>> fetchFoundForumPostList(){
        return ResponseEntity.ok(forumPostService.fetchFoundForumPostList());
    }

    @PutMapping("/")
    @RequiredScope(scope = SecurityScope.NONE)
    public ResponseEntity<ForumPost> updateForumPost(@Valid @RequestBody ForumPostDto ForumPostCreationInfo, @RequestParam("forumPostId") UUID ForumPostId){
        return ResponseEntity.ok(forumPostService.updateForumPost(ForumPostCreationInfo, ForumPostId));
    }

    @DeleteMapping("/")
    @RequiredScope(scope = SecurityScope.NONE)
    public ResponseEntity<Void> deleteForumPostById(@RequestParam("forumPostId") UUID ForumPostId){
        forumPostService.deleteForumPostById(ForumPostId);
        return ResponseEntity.ok(null);
    }
}




