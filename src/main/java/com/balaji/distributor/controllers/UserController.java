package com.balaji.distributor.controllers;

// USER CONTROLLER

import com.balaji.distributor.DTO.LoginRequest;
import com.balaji.distributor.DTO.UserActivity;
import com.balaji.distributor.DTO.UserUpdateRequest;
import com.balaji.distributor.entity.User;
import com.balaji.distributor.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.balaji.distributor.security.JwtService;
import com.balaji.distributor.security.LoginResponseSecurity;

import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    // SIGNUP API
    @PostMapping("/signup")
    public ResponseEntity<User> signup(@Valid @RequestBody User user) {

        User savedUser = userService.register(user);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedUser);
    }


    // use for Log-In
    @PostMapping("/login")
    public ResponseEntity<LoginResponseSecurity> login(

            @Valid
            @RequestBody
            LoginRequest request

    ) {

        User user =
                userService.login(request);

        String token =

                jwtService.generateToken(

                        user.getId(),

                        user.getMobileno(),

                        user.getRole()
                );

        LoginResponseSecurity response =
                new LoginResponseSecurity();

        response.setToken(
                token
        );

        response.setUserId(
                user.getId()
        );

        response.setUsername(
                user.getUsername()
        );

        response.setRole(
                user.getRole()
        );

        response.setMobileno(
                user.getMobileno()
        );

        response.setEmail(
                user.getEmail()
        );

        response.setShopname(
                user.getShopname()
        );

        response.setGstnumber(
                user.getGstnumber()
        );

        response.setAddressLine1(
                user.getAddressLine1()
        );

        response.setAddressLine2(
                user.getAddressLine2()
        );

        response.setCity(
                user.getCity()
        );

        response.setState(
                user.getState()
        );

        response.setPincode(
                user.getPincode()
        );

        return ResponseEntity.ok(
                response
        );
    }


    //  update user info
    @PutMapping("/updateinfo/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable Long id,
            @RequestBody UserUpdateRequest request
    ) {

        User updatedUser = userService.updateUser(id, request);

        return ResponseEntity.ok(updatedUser);
    }

    // GET ALL USERS
    // will be use for Admin in users section
    @GetMapping("/admin/all")
    public ResponseEntity<List<User>> getAllUsers() {

        return ResponseEntity.ok(userService.getAllUsers());
    }

    //block users
    @PutMapping("/block/{id}")
    public ResponseEntity<String> toggleBlockUser(@PathVariable Long id) {

        userService.toggleBlock(id);

        return ResponseEntity.ok("User status updated");
    }



    //Admin see users liftime purchase and orders
    @GetMapping(
            "/admin/activity"
    )
    public ResponseEntity<List<UserActivity>>
    getAllUserActivities() {

        return ResponseEntity.ok(

                userService
                        .getAllUserActivities()
        );
    }

}
