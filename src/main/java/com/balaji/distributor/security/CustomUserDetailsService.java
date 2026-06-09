package com.balaji.distributor.security;

import com.balaji.distributor.entity.User;
import com.balaji.distributor.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService
        implements UserDetailsService {

    private final UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(
            String mobileno
    ) throws UsernameNotFoundException {

        User user =
                userRepo.findByMobileno(
                                mobileno
                        )

                        .orElseThrow(

                                () -> new UsernameNotFoundException(
                                        "User not found"
                                )
                        );

        return org.springframework.security.core.userdetails.User

                .withUsername(
                        user.getMobileno()
                )

                .password(
                        user.getPassword()
                )

                .roles(
                        user.getRole()
                )

                .build();
    }
}