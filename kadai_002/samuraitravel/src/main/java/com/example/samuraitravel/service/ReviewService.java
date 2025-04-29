package com.example.samuraitravel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.Reviews;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.form.ReviewsEditForm;
import com.example.samuraitravel.form.ReviewsForm;
import com.example.samuraitravel.repository.ReviewsRepository;

@Service
public class ReviewService {
	
    @Autowired
    private ReviewsRepository reviewsRepository;
    
    public void createReviews(ReviewsForm reviewsForm,House house, User user) {
        Reviews reviews = new Reviews();
        reviews.setHouse(house);
        reviews.setUser(user);
        reviews.setStar(reviewsForm.getStar());
        reviews.setComment(reviewsForm.getComment());
        reviewsRepository.save(reviews);
    }
    
    public void updateReviews(Reviews reviews,ReviewsEditForm reviewsEditForm) {
    	reviews.setStar(reviewsEditForm.getStar());
    	reviews.setComment(reviewsEditForm.getComment());
    	reviewsRepository.save(reviews);
    }
}
