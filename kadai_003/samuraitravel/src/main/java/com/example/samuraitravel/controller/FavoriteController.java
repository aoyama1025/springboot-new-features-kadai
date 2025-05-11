package com.example.samuraitravel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.samuraitravel.entity.Favorite;
import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.repository.FavoriteRepository;
import com.example.samuraitravel.repository.HouseRepository;
import com.example.samuraitravel.security.UserDetailsImpl;
import com.example.samuraitravel.service.FavoriteService;

@Controller
public class FavoriteController {

	@Autowired
	private FavoriteService favoriteService;

	@Autowired
	private HouseRepository houseRepository;

	@Autowired
	private FavoriteRepository favoriteRepository;

	//お気に入り登録
	@PostMapping("/houses/{houseId}/favorites/create")
	public String createFavorite(@PathVariable("houseId") Integer houseId,
			@AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {

		House house = houseRepository.findById(houseId).orElseThrow();
		User user = userDetailsImpl.getUser();
		favoriteService.createFavorite(user, house);
		return "redirect:/houses/" + houseId;
	}

	//お気に入り削除
	@PostMapping("/houses/{houseId}/favorites/{favoriteId}/delete")
	public String deleteFavorite(@PathVariable("houseId") Integer houseId,
			@PathVariable("favoriteId") Integer favoriteId,
			@AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {

		House house = houseRepository.findById(houseId).orElseThrow();
		User user = userDetailsImpl.getUser();
		favoriteService.deleteFavorite(user, house);
		return "redirect:/houses/" + houseId;
	}

	//お気に入り一覧
	@GetMapping("/favorites")
	public String showFavorite(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
			@PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.ASC) Pageable pageable, Model model) {

		User user = userDetailsImpl.getUser();
		Page<Favorite> favorites = favoriteRepository.findByUser(user, pageable);

		model.addAttribute("favorites", favorites);

		return "houses/favorites";
	}
}
