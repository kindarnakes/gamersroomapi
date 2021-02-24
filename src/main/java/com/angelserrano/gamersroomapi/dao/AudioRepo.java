package com.angelserrano.gamersroomapi.dao;

import com.angelserrano.gamersroomapi.model.Audio;
import com.angelserrano.gamersroomapi.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AudioRepo extends JpaRepository<Audio, Integer> {
}
