package com.angelserrano.gamersroomapi.dao;

import com.angelserrano.gamersroomapi.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepo extends JpaRepository<Comment, Integer> {

    @Query(
            value = "SELECT c.* FROM comment c LEFT JOIN publication p on p.id = c.id_publication WHERE p.id = ?1",
            nativeQuery = true)
    List<Comment> getCommentsByPublication(int idpublication);

}
