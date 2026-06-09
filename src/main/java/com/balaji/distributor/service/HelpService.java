package com.balaji.distributor.service;

import com.balaji.distributor.entity.HelpRequest;
import com.balaji.distributor.entity.User;
import com.balaji.distributor.repository.HelpRepository;
import com.balaji.distributor.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import com.balaji.distributor.exceptionHandler.ResourceNotFoundException;

import java.util.List;

@Service
public class HelpService {

    @Autowired
    private HelpRepository helpRepo;

    @Autowired
    private UserRepository userRepo;


    // CREATE HELP REQUEST
    @Transactional
    public HelpRequest saveRequest(HelpRequest request, Long userId) {

        if(userId != null) {

            User user = userRepo.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "User not found"
                    ));

            request.setUser(user);
        }

        return helpRepo.save(request);
    }


    // USER REQUEST HISTORY

    public List<HelpRequest> getUserRequests(Long userId) {

        return helpRepo
                .findByUserIdOrderByIdDesc(
                        userId
                );
    }



    // ADMIN FETCHING HELP DB TO REVIEW


    //get list of all the requests
    public List<HelpRequest> getAllRequests() {

        return helpRepo.findAllByOrderByIdDesc();
    }


    @Transactional
    public HelpRequest markAsReviewed(Long id) {

        HelpRequest request = helpRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Request not found"
                ));

        request.setStatus("REVIEWED");

        return helpRepo.save(request);
    }


    @Transactional
    public void deleteRequest(Long id) {

        HelpRequest request = helpRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Request not found"
                ));

        helpRepo.delete(request);
    }
}