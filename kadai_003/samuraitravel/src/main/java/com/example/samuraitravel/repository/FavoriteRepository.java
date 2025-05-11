package com.example.samuraitravel.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.samuraitravel.entity.Favorite;
import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.User;

public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {

	//特定ユーザーのお気に入り一覧
	Page<Favorite> findByUser(User user, Pageable pageable);

	//ユーザーが特定の宿をお気に入り登録しているか
	Favorite findByUserAndHouse(User user, House house);

}
