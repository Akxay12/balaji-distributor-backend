package com.balaji.distributor.service;

// USER SERVICE

import com.balaji.distributor.DTO.LoginRequest;
import com.balaji.distributor.DTO.UserActivity;
import com.balaji.distributor.DTO.UserUpdateRequest;
import com.balaji.distributor.entity.User;
import com.balaji.distributor.exceptionHandler.BadRequestException;
import com.balaji.distributor.exceptionHandler.UserBlockedException;
import com.balaji.distributor.repository.OrderRepository;
import com.balaji.distributor.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.balaji.distributor.exceptionHandler.ResourceNotFoundException;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private BCryptPasswordEncoder
            passwordEncoder;

    @Autowired
    private OrderRepository orderRepo;


    // sign up api (creating new user)
    @Transactional
    public User register(User user) {


        if (

                user.getUsername() == null ||
                        user.getUsername().isBlank() ||

                        user.getMobileno() == null ||
                        user.getMobileno().isBlank() ||

                        user.getCity() == null ||
                        user.getCity().isBlank() ||

                        user.getShopname() == null ||
                        user.getShopname().isBlank() ||

                        user.getGstnumber() == null ||
                        user.getGstnumber().isBlank() ||

                        user.getPincode() == null ||


                        user.getPassword() == null ||
                        user.getPassword().isBlank()

        ) {

            throw new BadRequestException(
                    "All required fields must be filled"
            );
        }

        if (

                user.getPincode() < 100000L

                        ||

                        user.getPincode() > 999999L

        ) {

            throw new BadRequestException(
                    "Invalid pincode"
            );
        }

        if (
                !user.getUsername()
                        .matches("^[A-Za-z ]+$")
        ) {

            throw new BadRequestException(
                    "Full name can contain only letters and spaces"
            );
        }

        if (
                !user.getCity()
                        .matches("^[A-Za-z ]+$")
        ) {

            throw new BadRequestException(
                    "City can contain only letters and spaces"
            );
        }

        if (
                !user.getShopname()
                        .matches("^[A-Za-z0-9 ]+$")
        ) {

            throw new BadRequestException(
                    "Shop name can contain only letters, numbers and spaces"
            );
        }

        if (
                !user.getGstnumber()
                        .matches("^[A-Za-z0-9]+$")
        ) {

            throw new BadRequestException(
                    "GST number can contain only letters and numbers"
            );
        }


        // gst duplicate check

        if (
                userRepo.existsByGstnumber(
                        user.getGstnumber()
                )
        ) {

            throw new BadRequestException(
                    "GST number already exists"
            );
        }

        if (
                !user.getMobileno()
                        .matches("\\d{10}")
        ) {

            throw new BadRequestException(
                    "Invalid mobile number"
            );
        }

        // mobile duplicate check
        if (userRepo.existsByMobileno(((user.getMobileno())))) {
            throw new BadRequestException(
                    "Mobile number already exists"
            );
        }

        user.setPassword(
                passwordEncoder.encode(
                        user.getPassword()
                )
        );

        return userRepo.save(user);
    }


    // login api (is user exist)
    // login api (is user exist)
    public User login(LoginRequest request) {

        if (
                request.getMobileno() == null
                        ||
                        request.getMobileno().isBlank()
                        ||
                        request.getPassword() == null
                        ||
                        request.getPassword().isBlank()
        ) {

            throw new BadRequestException(
                    "Mobile number and password are required"
            );
        }


        User user = userRepo.findByMobileno(
                        request.getMobileno()
                )
                .orElseThrow(() ->
                        new BadRequestException(
                                "Invalid credentials"
                        )
                );

        if (
                !passwordEncoder.matches(
                        request.getPassword(),
                        user.getPassword()
                )
        ) {

            throw new BadRequestException(
                    "Invalid credentials"
            );
        }

        if (user.getBlocked()) {

            throw new UserBlockedException(
                    "Your account has been blocked by admin"
            );
        }

        return user;
    }


    // update user info
    @Transactional
    public User updateUser(Long id, UserUpdateRequest request) {

        User existingUser = userRepo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );

        // USERNAME

        if (
                request.getUsername() != null
        ) {

            if (
                    request.getUsername().isBlank()
            ) {

                throw new BadRequestException(
                        "Username cannot be blank"
                );
            }

            if (
                    !request.getUsername()
                            .matches("^[A-Za-z ]+$")
            ) {

                throw new BadRequestException(
                        "Full name can contain only letters and spaces"
                );
            }

            existingUser.setUsername(
                    request.getUsername()
            );
        }

        // CITY

        if (
                request.getCity() != null
        ) {

            if (
                    !request.getCity().isBlank()
                            &&
                            !request.getCity()
                                    .matches("^[A-Za-z ]+$")
            ) {

                throw new BadRequestException(
                        "City can contain only letters and spaces"
                );
            }

            existingUser.setCity(
                    request.getCity()
            );
        }

        // SHOP NAME

        if (
                request.getShopname() != null
        ) {

            if (
                    !request.getShopname()
                            .matches("^[A-Za-z0-9 ]+$")
            ) {

                throw new BadRequestException(
                        "Shop name can contain only letters, numbers and spaces"
                );
            }

            existingUser.setShopname(
                    request.getShopname()
            );
        }

        // GST FORMAT

        if (
                request.getGstnumber() != null
        ) {

            if (
                    !request.getGstnumber()
                            .matches("^[A-Za-z0-9]+$")
            ) {

                throw new BadRequestException(
                        "GST number can contain only letters and numbers"
                );
            }

            // GST DUPLICATE CHECK

            if (
                    userRepo.existsByGstnumber(
                            request.getGstnumber()
                    )
                            &&
                            (
                                    existingUser.getGstnumber() == null
                                            ||
                                            !existingUser.getGstnumber()
                                                    .equalsIgnoreCase(
                                                            request.getGstnumber()
                                                    )
                            )
            ) {

                throw new BadRequestException(
                        "GST number already exists"
                );
            }

            existingUser.setGstnumber(
                    request.getGstnumber()
            );
        }

        // EMAIL

        if (
                request.getEmail() != null
        ) {

            existingUser.setEmail(
                    request.getEmail()
            );
        }

        // ADDRESS

        if (
                request.getAddressLine1() != null
        ) {

            existingUser.setAddressLine1(
                    request.getAddressLine1()
            );
        }

        if (
                request.getAddressLine2() != null
        ) {

            existingUser.setAddressLine2(
                    request.getAddressLine2()
            );
        }

        if (
                request.getState() != null
        ) {

            existingUser.setState(
                    request.getState()
            );
        }

        // PINCODE

        if (
                request.getPincode() != null
        ) {

            if (
                    request.getPincode() < 100000L
                            ||
                            request.getPincode() > 999999L
            ) {

                throw new BadRequestException(
                        "Invalid pincode"
                );
            }

            existingUser.setPincode(
                    request.getPincode()
            );
        }

        // PASSWORD

        if (
                request.getPassword() != null
                        &&
                        !request.getPassword().isBlank()
        ) {

            existingUser.setPassword(
                    passwordEncoder.encode(
                            request.getPassword()
                    )
            );
        }

        return userRepo.save(
                existingUser
        );
    }


    //  for admin user panel
    public List<User> getAllUsers() {
        return userRepo.findByRole("USER");
    }


    // for blocking user
    @Transactional
    public User toggleBlock(Long id) {

        User user = userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));


        user.setBlocked(!user.getBlocked());

        User savedUser = userRepo.save(user);


        return savedUser;
    }


    // for admin to see users purcchases and orders
    public UserActivity getUserActivity(
            Long userId
    ) {

        User user = userRepo.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );

        long totalOrders =
                orderRepo.countByUserId(
                        userId
                );

        Double lifetimePurchase =
                orderRepo.getLifetimePurchase(
                        userId
                );

        return new UserActivity(

                user.getId(),

                totalOrders,

                lifetimePurchase
        );
    }




    public List<UserActivity>
    getAllUserActivities() {

        List<User> users =
                userRepo.findByRole(
                        "USER"
                );

        return users.stream()

                .map(user -> {

                    long orders =
                            orderRepo.countByUserId(
                                    user.getId()
                            );

                    Double purchase =
                            orderRepo.getLifetimePurchase(
                                    user.getId()
                            );

                    return new UserActivity(

                            user.getId(),

                            orders,

                            purchase
                    );
                })

                .toList();
    }

}