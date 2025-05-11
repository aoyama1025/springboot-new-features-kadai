package com.example.samuraitravel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.Reviews;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.form.ReviewsEditForm;
import com.example.samuraitravel.form.ReviewsForm;
import com.example.samuraitravel.repository.HouseRepository;
import com.example.samuraitravel.repository.ReviewsRepository;
import com.example.samuraitravel.repository.UserRepository;
import com.example.samuraitravel.security.UserDetailsImpl;
import com.example.samuraitravel.service.ReviewService;

@Controller
public class ReviewController {

	@Autowired
	private ReviewService reviewService;

	@Autowired
	private HouseRepository houseRepository;

	@Autowired
	private ReviewsRepository reviewsRepository;

	@Autowired
	private UserRepository userRepository;
	

	//レビュー登録
	@PostMapping("/houses/{houseId}/reviews/register")
	public String createReview(@PathVariable("houseId") Integer houseId, @Validated ReviewsForm reviewsForm,
			BindingResult bindingResult,
			@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, Model model) {

		House house = houseRepository.findById(houseId).orElse(null);
		User user = userDetailsImpl.getUser();

		if (bindingResult.hasErrors()) {
			model.addAttribute("house", house);
			model.addAttribute("reviewsForm", reviewsForm);
			return "houses/reviewsform"; // レビュー投稿フォームに戻る
		}

		try {
			reviewService.createReviews(reviewsForm, house, user);
			return "redirect:/houses/" + houseId; //成功時
		} catch (DataAccessException e) {
			// データベースエラーの処理
			model.addAttribute("errorMessage", "レビューの投稿ができませんでした。");
			model.addAttribute("house", house);
			model.addAttribute("reviewsForm", reviewsForm);
			return "houses/reviewsform"; // レビュー投稿フォームに戻る
		}
	}

	//レビュー投稿フォームの表示
	@GetMapping("/houses/{houseId}/reviews/form")
	public String reviewsForm(@PathVariable("houseId") Integer houseId, Model model) {
		House house = houseRepository.findById(houseId).orElse(null);
		if (house != null) {
			model.addAttribute("house", house);
			model.addAttribute("reviewsForm", new ReviewsForm());
			return "houses/reviewsform";
		} else {
			return "redirect:/houses";
		}
	}

	//レビュー編集フォームの表示
	@GetMapping("/houses/{houseId}/reviews/{reviewId}/edit")
	public String edit(@PathVariable("houseId") Integer houseId, @PathVariable("reviewId") Integer reviewId,
			@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, Model model) {

		House house = houseRepository.findById(houseId).orElse(null);
		Reviews review = reviewsRepository.findById(reviewId).orElse(null);
		User user = userDetailsImpl.getUser();

		if (house == null || review == null) {
			return "redirect:/houses/" + houseId;
		}

		//レビューを書いたユーザーのIDとログインユーザーのIDが違うとき
		if (!review.getUser().getId().equals(user.getId())) {
			return "error/403"; // アクセス拒否
		}

		ReviewsEditForm reviewsEditForm = new ReviewsEditForm(review.getStar(), review.getComment());

		model.addAttribute("house", house);
		model.addAttribute("reviewsEditForm", reviewsEditForm);
		model.addAttribute("reviewId", reviewId);
		return "houses/edit"; // レビュー編集フォームに戻る
	}

	//レビュー更新
	@PostMapping("/houses/{houseId}/reviews/{reviewId}/update")
	public String update(@PathVariable("houseId") Integer houseId, @PathVariable("reviewId") Integer reviewId,
			@Validated ReviewsEditForm reviewsEditForm,
			BindingResult bindingResult,
			@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, Model model) {

		House house = houseRepository.findById(houseId).orElse(null);
		Reviews review = reviewsRepository.findById(reviewId).orElse(null);
		User user = userDetailsImpl.getUser();

		if (house == null || review == null) {
			return "redirect:/houses/" + houseId;
		}

		//レビューを書いたユーザーのIDとログインユーザーのIDが違うとき
		if (!review.getUser().getId().equals(user.getId())) {
			return "error/403"; // アクセス拒否
		}

		if (bindingResult.hasErrors()) {
			model.addAttribute("house", house);
			model.addAttribute("reviewsEditForm", reviewsEditForm);
			model.addAttribute("reviewId", reviewId);
			return "houses/edit"; // レビュー編集フォームに戻る
		}

		try {
			reviewService.updateReviews(review, reviewsEditForm);
			return "redirect:/houses/" + houseId; //成功時
		} catch (DataAccessException e) {
			// データベースエラーの処理
			model.addAttribute("errorMessage", "レビューの更新ができませんでした。");
			model.addAttribute("house", house);
			model.addAttribute("reviewsEditForm", reviewsEditForm);
			model.addAttribute("reviewId", reviewId);
			return "houses/edit"; // レビュー投稿フォームに戻る
		}
	}

	//レビュー削除
	@PostMapping("/houses/{houseId}/reviews/{reviewId}/delete")
	public String delete(@PathVariable("houseId") Integer houseId, @PathVariable("reviewId") Integer reviewId,
			@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
			Model model) {

		Reviews review = reviewsRepository.findById(reviewId).orElse(null);
		User user = userDetailsImpl.getUser();

		//レビューを書いたユーザーのIDとログインユーザーのIDが違うとき
		if (!review.getUser().getId().equals(user.getId())) {
			return "error/403"; //アクセス拒否
		}
		try {
			reviewsRepository.delete(review);
			return "redirect:/houses/" + houseId;
		} catch (DataAccessException e) {
			model.addAttribute("errorMessage", "レビューの削除に失敗しました。");
			return "redirect:/houses/" + houseId;
		}
	}

	//レビュー一覧ページの表示
	@GetMapping("/houses/{houseId}/reviews")
	public String showReviewList(@PathVariable("houseId") Integer houseId,
			@PageableDefault(page = 0, size = 10, sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable,Model model) {
	Page<Reviews> reviewPage = reviewsRepository.findByHouseId(houseId, pageable);
	House house = houseRepository.findById(houseId).orElse(null); //
	
	model.addAttribute("house", house);
    model.addAttribute("reviewPage", reviewPage);
    
    return "houses/reviewslist";
	
	}
}