package com.angelserrano.gamersroomapi.service;

import com.angelserrano.gamersroomapi.dao.CommentRepo;
import com.angelserrano.gamersroomapi.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    @Autowired
    CommentRepo repository;


    public List<Comment> getAllItems() {
        List<Comment> itemList = repository.findAll();

        if (itemList.size() > 0) {
            return itemList;
        } else {
            return new ArrayList<>();
        }
    }


    public Comment getCommentById(int id) {
        Optional<Comment> item = repository.findById(id);

        if (item.isPresent()) {
            return item.get();
        } else {
            return new Comment();
        }
    }

    public Comment createComment(Comment entity) {
        entity.setDate(LocalDateTime.now());
        entity = repository.save(entity);
        return entity;
    }

    public void deleteItemById(int id) {
        Optional<Comment> item = repository.findById(id);

        if (item.isPresent()) {
            repository.deleteById(id);
        } else {
        }
    }

    public Comment updateComment(Comment c){
        Optional<Comment> item = repository.findById(c.getId());

        if(item.isPresent()){
            Comment co = item.get();
            co.setUser(c.getUser());
            co.setText(c.getText());
            co.setDate(c.getDate());

            co = repository.save(co);
            return co;
        }
        return new Comment();

    }

    public List<Comment> getCommentsByPublication(int idpublication){
        List<Comment> itemList = repository.getCommentsByPublication(idpublication);

        if (itemList.size() > 0) {
            return itemList;
        } else {
            return new ArrayList<>();
        }

    }

}
