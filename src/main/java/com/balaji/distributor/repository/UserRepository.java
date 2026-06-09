package com.balaji.distributor.repository;

// USER REPOSITORY

import com.balaji.distributor.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByMobileno(String mobileno);

    boolean existsByGstnumber(String gstnumber);

    Optional<User> findByMobileno(String mobileno);

    List<User> findByRole(String role);
}
