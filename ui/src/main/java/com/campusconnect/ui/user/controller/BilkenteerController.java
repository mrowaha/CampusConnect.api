package com.campusconnect.ui.user.controller;

import com.campusconnect.domain.user.dto.ProtectedDto;
import com.campusconnect.ui.common.controller.SecureController;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/bilkenteer")
public class BilkenteerController extends SecureController {

    private static final String BASE_URL = "/bilkenteer";

    @GetMapping(value = "/s")
    public ResponseEntity<ProtectedDto> protectedBilkenteerRoute() {
        System.out.println("in bilkenteer protected");
        return new ResponseEntity<>(new ProtectedDto("Authorized"), HttpStatus.OK);
    }


    @Override
    public void postConstruct() {
        this.addEndpoint(HttpMethod.GET, BASE_URL,"/s", SecurityScope.BILKENTEER);
    }
}
