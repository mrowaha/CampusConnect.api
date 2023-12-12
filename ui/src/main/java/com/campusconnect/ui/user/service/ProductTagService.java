package com.campusconnect.ui.common.service;

import org.springframework.stereotype.Service;

@Service
public class ProductTagService {

    public String requestProductTag(String tagName) {
        return "Tag request for " + tagName + " received.";
    }
}
