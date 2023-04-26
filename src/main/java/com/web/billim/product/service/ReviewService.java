package com.web.billim.product.service;

import com.web.billim.product.domain.Review;
import com.web.billim.product.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

	private final ReviewRepository reviewRepository;

	public double calculateStarRating(int productId) {
		return reviewRepository.findAllByProductId(productId).stream()
			.collect(Collectors.summarizingInt(Review::getStarRating))
			.getAverage();
	}

}
