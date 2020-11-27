package com.unicap.tcc.usability.api.service;


import com.unicap.tcc.usability.api.models.User;
import com.unicap.tcc.usability.api.models.assessment.Assessment;
import com.unicap.tcc.usability.api.models.assessment.AssessmentUserGroup;
import com.unicap.tcc.usability.api.models.dto.review.BeginReviewDTO;
import com.unicap.tcc.usability.api.models.dto.review.ReviewListResponseDTO;
import com.unicap.tcc.usability.api.models.dto.review.ReviewRequestDTO;
import com.unicap.tcc.usability.api.models.enums.AssessmentState;
import com.unicap.tcc.usability.api.models.enums.EReviewState;
import com.unicap.tcc.usability.api.models.enums.SectionEnum;
import com.unicap.tcc.usability.api.models.review.Comment;
import com.unicap.tcc.usability.api.models.review.Review;
import com.unicap.tcc.usability.api.repository.AssessmentRepository;
import com.unicap.tcc.usability.api.repository.AssessmentUserGroupRepository;
import com.unicap.tcc.usability.api.repository.ReviewRepository;
import com.unicap.tcc.usability.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final AssessmentUserGroupRepository assessmentUserGroupRepository;
    private final AssessmentRepository assessmentRepository;
    private final MailSender mailSender;

    public Optional<Review> submitNewReview(ReviewRequestDTO reviewRequestDTO) {
        var optionalAssessment = assessmentRepository.findByUid(reviewRequestDTO.getAssessmentUid());
        if (optionalAssessment.isPresent()) {
            var newReview = Review.builder()
                    .assessment(optionalAssessment.get())
                    .uid(UUID.randomUUID())
                    .limitReviewDate(reviewRequestDTO.getDateLimit())
                    .comments(SectionEnum.getSectionList()
                            .stream()
                            .map(sectionEnum ->
                                    Comment.builder()
                                            .section(sectionEnum)
                                            .comment("")
                                            .build()).collect(Collectors.toSet()))
                    .state(EReviewState.AVAILABLE)
                    .build();
            reviewRepository.save(newReview);
            optionalAssessment.get().setState(AssessmentState.WAITING_REVIEW);
            assessmentRepository.save(optionalAssessment.get());
            var userGroup =
                    assessmentUserGroupRepository.findAllByAssessmentAndAssessmentRemovedDateIsNull(optionalAssessment.get());
            var collaboratorsList =
                    userGroup.stream().map(AssessmentUserGroup::getSystemUser).collect(Collectors.toList());
            var reviwerList =
                    userRepository.findAllByIsReviewerTrueAndRemovedDateIsNullAndUidNotIn(
                            collaboratorsList.stream().map(User::getUid).collect(Collectors.toList()));
            var reviewerEmailList = reviwerList.stream().map(User::getEmail).collect(Collectors.toList());
            mailSender.sendAvailableReviewEmail(optionalAssessment.get(), newReview, reviewerEmailList);
            return Optional.of(newReview);
        }
        return Optional.empty();
    }

    public Set<ReviewListResponseDTO> getAvailableReviews(UUID userUid) {
        var optionalUser = userRepository.findByUidAndRemovedDateIsNull(userUid);
        if (optionalUser.isPresent()) {
            var reviewerReviews = reviewRepository.findAllByReviewerUidAndRemovedDateIsNull(userUid)
                    .stream()
                    .map(review -> ReviewListResponseDTO.builder()
                            .limitReviewDate(review.getLimitReviewDate())
                            .projectName(review.getAssessment().getProjectName())
                            .reviewStatus(review.getState())
                            .reviewUid(review.getUid())
                            .build())
                    .collect(Collectors.toSet());
            var reviewList = reviewRepository.findAllWhereUserEvaluatorIsNotReviewer(optionalUser.get().getId());
            reviewerReviews.addAll(reviewList.stream().map(review -> ReviewListResponseDTO.builder()
                    .limitReviewDate(review.getLimitReviewDate())
                    .projectName(review.getAssessment().getProjectName())
                    .reviewStatus(review.getState())
                    .reviewUid(review.getUid())
                    .build()).collect(Collectors.toSet()));
            return reviewerReviews;
        }
        return Collections.emptySet();
    }

    public Set<ReviewListResponseDTO> getReviewingPlanList(UUID userUid) {
        var optionalUser = userRepository.findByUidAndRemovedDateIsNull(userUid);
        if (optionalUser.isPresent()) {
            var reviewerReviews = reviewRepository.findAllByReviewerUidAndRemovedDateIsNull(userUid)
                    .stream()
                    .map(review -> ReviewListResponseDTO.builder()
                            .limitReviewDate(review.getLimitReviewDate())
                            .projectName(review.getAssessment().getProjectName())
                            .reviewStatus(review.getState())
                            .build())
                    .collect(Collectors.toSet());
            var reviewList = reviewRepository.findAllWhereUserEvaluatorIsNotReviewer(optionalUser.get().getId());
            reviewerReviews.addAll(reviewList.stream()
                    .map(review ->
                            ReviewListResponseDTO.builder()
                                    .limitReviewDate(review.getLimitReviewDate())
                                    .projectName(review.getAssessment().getProjectName())
                                    .reviewStatus(review.getState())
                                    .build())
                    .collect(Collectors.toSet()));
            return reviewerReviews;
        }
        return Collections.emptySet();
    }


    public Optional<Review> findReviewAssessment(BeginReviewDTO beginReviewDTO) {
        return reviewRepository.findByUidAndRemovedDateIsNull(beginReviewDTO.getReviewUid());
    }

    public Optional<Review> beginReview(BeginReviewDTO beginReviewDTO) {
        var optionalReview = reviewRepository.findByUidAndRemovedDateIsNull(beginReviewDTO.getReviewUid());
        if (optionalReview.isPresent()) {
            var optionalReviewer = userRepository.findByUidAndRemovedDateIsNull(beginReviewDTO.getUserUid());
            if (optionalReviewer.isPresent()) {
                optionalReview.get().setReviewer(optionalReviewer.get());
                optionalReview.get().setState(EReviewState.REVIEWING);
                optionalReview.get().getAssessment().setState(AssessmentState.IN_REVIEW);
                reviewRepository.save(optionalReview.get());
                return optionalReview;
            }
        }
        return Optional.empty();
    }
}
