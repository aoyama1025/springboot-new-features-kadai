package com.example.samuraitravel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.samuraitravel.entity.Favorite;
import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.repository.FavoriteRepository;

import jakarta.transaction.Transactional;

@Service
public class FavoriteService {

	@Autowired
	private FavoriteRepository favoriteRepository;

	//お気に入り登録されているか
	public boolean isFavorite(User user, House house) {
		return favoriteRepository.findByUserAndHouse(user, house) != null;
	}

	//お気に入り登録
	@Transactional
	public void createFavorite(User user, House house) {
		if (!isFavorite(user, house)) {
			Favorite favorite = new Favorite();
			favorite.setUser(user);
			favorite.setHouse(house);
			favoriteRepository.save(favorite);
		}
	}

	//お気に入り削除
	@Transactional
	public void deleteFavorite(User user, House house) {
		Favorite favoriteToDelete = favoriteRepository.findByUserAndHouse(user, house);
		if (favoriteToDelete != null) {
			favoriteRepository.delete(favoriteToDelete);
		}
	}

	//特定のユーザーと民宿のお気に入りエンティティを取得
	public Favorite findByUserAndHouse(User user, House house) {
        return favoriteRepository.findByUserAndHouse(user, house);
    }
}


