package com.angelserrano.gamersroomapi.dao;

import com.angelserrano.gamersroomapi.model.Coordinates;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoordinatesRepo extends JpaRepository<Coordinates, Integer> {

}
