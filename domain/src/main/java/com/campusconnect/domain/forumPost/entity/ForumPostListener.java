package com.campusconnect.domain.forumPost.entity;

import jakarta.persistence.PostLoad;

public class ForumPostListener {

    @PostLoad
    public void incrementViewCount(ForumPost forumPost) {
        forumPost.setViewCount(forumPost.getViewCount() + 1);
    }
}