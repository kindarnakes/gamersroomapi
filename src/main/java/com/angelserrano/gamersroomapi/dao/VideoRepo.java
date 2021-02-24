package com.angelserrano.gamersroomapi.dao;

import com.angelserrano.gamersroomapi.model.Image;
import com.angelserrano.gamersroomapi.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoRepo extends JpaRepository<Video, Integer> {
}
