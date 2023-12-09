package com.campusconnect.ui.user.controller;

import com.campusconnect.domain.user.dto.ProtectedDto;
import com.campusconnect.ui.common.controller.SecureController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bilkenteer")
public class BilkenteerController extends SecureController {


    @GetMapping(value = "/s")
    public ResponseEntity<ProtectedDto> protectedBilkenteerRoute() {
        return new ResponseEntity<>(new ProtectedDto("Authorized"), HttpStatus.OK);
    }


    @Override
    public void postConstruct() {
        System.out.println("BilkenteerController Init");
    }
}
