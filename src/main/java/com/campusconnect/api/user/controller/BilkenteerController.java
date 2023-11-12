package com.campusconnect.api.user.controller;

import java.util.List;
import com.campusconnect.api.user.entity.Bilkenteer;
import com.campusconnect.api.user.repository.BilkenteerRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.awt.*;

@RestController
public class BilkenteerController {

    private BilkenteerRepository bilkenteerRepository;

    @GetMapping(value="/bilkenteers", produces = "application/json")
    public List<Bilkenteer> getAllBilkenteers() {
        return bilkenteerRepository.findAll();
    }

    @PostMapping(value="/bilkenteers", consumes = "application/json")
    public void postBilkenteer(
            @RequestBody Bilkenteer bilkenteer
    ) {
        bilkenteerRepository.save(bilkenteer);
    }

}
