package com.angelserrano.gamersroomapi.dao;

import com.angelserrano.gamersroomapi.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepo extends JpaRepository<Image, Integer> {
}
