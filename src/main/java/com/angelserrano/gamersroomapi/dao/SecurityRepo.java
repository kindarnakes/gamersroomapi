package com.angelserrano.gamersroomapi.dao;


import com.angelserrano.gamersroomapi.model.SecurityToken;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SecurityRepo extends JpaRepository<SecurityToken, String> {
}
