package com.example.samuraitravel.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.samuraitravel.entity.Reviews;

public interface ReviewsRepository extends JpaRepository<Reviews, Integer> {
	Page<Reviews> findByHouseId(Integer houseId, Pageable pageable);
}
	