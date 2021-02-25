package com.angelserrano.gamersroomapi.api;

import com.angelserrano.gamersroomapi.dao.SecurityRepo;
import com.angelserrano.gamersroomapi.model.Comment;
import com.angelserrano.gamersroomapi.model.Publication;
import com.angelserrano.gamersroomapi.model.SecurityToken;
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
import java.util.Optional;

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
    @Autowired
    SecurityRepo securityRepo;


    @GetMapping
    public ResponseEntity<List<Comment>> getAllItems(@RequestHeader(name="key", required = false) String key) {

        Optional<SecurityToken> security = securityRepo.findById(key!=null?key:"");
        if(security.isPresent()) {
            SecurityToken.SECURITY_LEVEL securityLevel = securityRepo.findById(key).get().getSecurity_level();
            if (securityLevel == SecurityToken.SECURITY_LEVEL.ADMIN) {
                List<Comment> list = service.getAllItems();

                return new ResponseEntity<List<Comment>>(list, new HttpHeaders(), HttpStatus.OK);
            } else {
                return new ResponseEntity<List<Comment>>(null, new HttpHeaders(), HttpStatus.UNAUTHORIZED);
            }
        }else{
            return new ResponseEntity<List<Comment>>(null, new HttpHeaders(), HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Comment> getCommentById(@PathVariable("id") int id, @RequestHeader(name="key", required = false) String key) {

        Optional<SecurityToken> security = securityRepo.findById(key!=null?key:"");
        if(security.isPresent()) {
            SecurityToken.SECURITY_LEVEL securityLevel = securityRepo.findById(key).get().getSecurity_level();
            if (securityLevel == SecurityToken.SECURITY_LEVEL.USER || securityLevel == SecurityToken.SECURITY_LEVEL.ADMIN) {
                Comment entity = service.getCommentById(id);

                return new ResponseEntity<Comment>(entity, new HttpHeaders(), HttpStatus.OK);
            } else {
                return new ResponseEntity<Comment>(null, new HttpHeaders(), HttpStatus.UNAUTHORIZED);
            }
        }else{
            return new ResponseEntity<Comment>(null, new HttpHeaders(), HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping
    public ResponseEntity<Comment> createComment(@Valid @RequestBody Comment myItem, @RequestHeader(name="key", required = false) String key) {

        Optional<SecurityToken> security = securityRepo.findById(key!=null?key:"");
        if(security.isPresent()) {
            SecurityToken.SECURITY_LEVEL securityLevel = securityRepo.findById(key).get().getSecurity_level();
            if (securityLevel == SecurityToken.SECURITY_LEVEL.USER || securityLevel == SecurityToken.SECURITY_LEVEL.ADMIN) {
                Publication p = publicationService.getPublicationById(myItem.getPublication().getId());
                User u = userService.getUserById(myItem.getUser().getId());
                myItem.setUser(u);
                myItem.setPublication(p);
                try {
                    Comment created = service.createComment(myItem);
                    return new ResponseEntity<Comment>(created, new HttpHeaders(), HttpStatus.OK);
                } catch (Exception e) {
                    return new ResponseEntity<Comment>(null, new HttpHeaders(), HttpStatus.LOCKED);
                }
            } else {
                return new ResponseEntity<Comment>(null, new HttpHeaders(), HttpStatus.UNAUTHORIZED);
            }
        }else{
            return new ResponseEntity<Comment>(null, new HttpHeaders(), HttpStatus.UNAUTHORIZED);
        }

    }
    @PutMapping("/{id}")
    public ResponseEntity<Comment> updateComment(@PathVariable("id") int id, @Valid @RequestBody Comment myItem, @RequestHeader(name="key", required = false) String key) {

        Optional<SecurityToken> security = securityRepo.findById(key!=null?key:"");
        if(security.isPresent()) {
            SecurityToken.SECURITY_LEVEL securityLevel = securityRepo.findById(key).get().getSecurity_level();
            if (securityLevel == SecurityToken.SECURITY_LEVEL.USER || securityLevel == SecurityToken.SECURITY_LEVEL.ADMIN) {
                Publication p = publicationService.getPublicationById(myItem.getPublication().getId());
                User u = userService.getUserById(myItem.getUser().getId());
                myItem.setUser(u);
                myItem.setPublication(p);
                try {
                    Comment created = service.updateComment(myItem);
                    return new ResponseEntity<Comment>(created, new HttpHeaders(), HttpStatus.OK);
                } catch (Exception e) {
                    return new ResponseEntity<Comment>(null, new HttpHeaders(), HttpStatus.LOCKED);
                }
            } else {
                return new ResponseEntity<Comment>(null, new HttpHeaders(), HttpStatus.UNAUTHORIZED);
            }
        }else{
            return new ResponseEntity<Comment>(null, new HttpHeaders(), HttpStatus.UNAUTHORIZED);
        }

    }



    @DeleteMapping("/{id}")
    public HttpStatus deleteCommentById(@PathVariable("id") int id, @RequestHeader(name="key", required = false) String key) {

        Optional<SecurityToken> security = securityRepo.findById(key!=null?key:"");
        if(security.isPresent()) {
            SecurityToken.SECURITY_LEVEL securityLevel = securityRepo.findById(key).get().getSecurity_level();
            if (securityLevel == SecurityToken.SECURITY_LEVEL.USER || securityLevel == SecurityToken.SECURITY_LEVEL.ADMIN) {
                service.deleteItemById(id);
                return HttpStatus.ACCEPTED;
            } else {
                return HttpStatus.UNAUTHORIZED;
            }
        }else{
            return HttpStatus.UNAUTHORIZED;
        }
    }

    @DeleteMapping("/publication/{id}")
    public ResponseEntity<List<Comment>> getCommentsByPublication(@PathVariable("id") int id, @RequestHeader(name="key", required = false) String key) {
        Optional<SecurityToken> security = securityRepo.findById(key!=null?key:"");
        if(security.isPresent()) {
            SecurityToken.SECURITY_LEVEL securityLevel = securityRepo.findById(key).get().getSecurity_level();
            if (securityLevel == SecurityToken.SECURITY_LEVEL.USER || securityLevel == SecurityToken.SECURITY_LEVEL.ADMIN) {

                List<Comment> list = service.getCommentsByPublication(id);

                return new ResponseEntity<List<Comment>>(list, new HttpHeaders(), HttpStatus.OK);
            } else {
                return new ResponseEntity<List<Comment>>(null, new HttpHeaders(), HttpStatus.UNAUTHORIZED);
            }
        }else{
            return new ResponseEntity<List<Comment>>(null, new HttpHeaders(), HttpStatus.UNAUTHORIZED);
        }
    }
}
