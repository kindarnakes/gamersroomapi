package com.angelserrano.gamersroomapi.api;

import com.angelserrano.gamersroomapi.model.Comment;
import com.angelserrano.gamersroomapi.model.Publication;
import com.angelserrano.gamersroomapi.model.User;
import com.angelserrano.gamersroomapi.service.CommentService;
import com.angelserrano.gamersroomapi.service.PublicationService;
import com.angelserrano.gamersroomapi.service.UserService;
import jdk.swing.interop.SwingInterOpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/comment")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CommentController {


    @Autowired
    CommentService service;
    @Autowired
    PublicationService publicationService;
    @Autowired
    UserService userService;


    @GetMapping
    public ResponseEntity<List<Comment>> getAllItems() {
        List<Comment> list = service.getAllItems();

        return new ResponseEntity<List<Comment>>(list, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Comment> getCommentById(@PathVariable("id") int id) {
        Comment entity = service.getCommentById(id);

        return new ResponseEntity<Comment>(entity, new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> createComment(@Valid @RequestBody Comment myItem) {
        Publication p = publicationService.getPublicationById(myItem.getPublication().getId());
        User u = userService.getUserById(myItem.getUser().getId());
        myItem.setUser(u);
        myItem.setPublication(p);
        try {
            Comment created = service.createComment(myItem);
            return new ResponseEntity<Object>(created, new HttpHeaders(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<Object>(-1, new HttpHeaders(), HttpStatus.LOCKED);
        }

    }

    @DeleteMapping("/{id}")
    public HttpStatus deleteCommentById(@PathVariable("id") int id) {
        service.deleteItemById(id);
        return HttpStatus.ACCEPTED;
    }

    @DeleteMapping("/publication/{id}")
    public ResponseEntity<List<Comment>> getCommentsByPublication(@PathVariable("id") int id) {
        List<Comment> list = service.getCommentsByPublication(id);

        return new ResponseEntity<List<Comment>>(list, new HttpHeaders(), HttpStatus.OK);
    }
}
