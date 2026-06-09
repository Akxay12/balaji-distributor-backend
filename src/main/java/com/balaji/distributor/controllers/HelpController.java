package com.balaji.distributor.controllers;

import com.balaji.distributor.entity.HelpRequest;
import com.balaji.distributor.security.JwtService;
import com.balaji.distributor.service.HelpService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/help")
@CrossOrigin("*")
public class HelpController {

    @Autowired
    private HelpService helpService;

    @Autowired
    private JwtService jwtService;

    // SEND HELP REQUEST
    @PostMapping("/user/sendrequest")
    public ResponseEntity<HelpRequest> submitRequest(

            @Valid @RequestBody HelpRequest request,

            @RequestParam(required = false) Long userId
    ) {

        HelpRequest savedRequest =
                helpService.saveRequest(request, userId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedRequest);
    }


    // USER REQUEST HISTORY
    @GetMapping("/user/history")
    public ResponseEntity<List<HelpRequest>> getUserHistory(

            @RequestHeader("Authorization")
            String authHeader
    ) {

        String token =
                authHeader.substring(7);

        Long userId =
                jwtService.extractUserId(token);

        return ResponseEntity.ok(
                helpService.getUserRequests(userId)
        );
    }



    //FOR ADMIN FETCH HELREQUESTS
    @GetMapping("/admin/allrequests")
    public ResponseEntity<List<HelpRequest>> getAllRequests() {

        return ResponseEntity.ok(
                helpService.getAllRequests()
        );
    }


    //mark as review
    @PutMapping("/admin/review/{id}")
    public ResponseEntity<HelpRequest> markAsReviewed(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                helpService.markAsReviewed(id)
        );
    }

    // for Adminn to delete query
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<String> deleteRequest(
            @PathVariable Long id
    ) {

        helpService.deleteRequest(id);

        return ResponseEntity.ok("Request deleted successfully");
    }



}