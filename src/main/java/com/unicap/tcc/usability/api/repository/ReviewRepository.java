package com.unicap.tcc.usability.api.repository;

import com.unicap.tcc.usability.api.models.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

}