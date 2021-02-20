package com.angelserrano.gamersroomapi.api;

import com.angelserrano.gamersroomapi.Utils.Cloud;
import com.angelserrano.gamersroomapi.model.Publication;
import com.angelserrano.gamersroomapi.model.User;
import com.angelserrano.gamersroomapi.service.PublicationService;
import com.angelserrano.gamersroomapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/publication")
public class PublicationController {
    @Autowired
    PublicationService service;
    @Autowired
    UserService userService;

    @GetMapping
    public ResponseEntity<List<Publication>> getAllItems() {
        List<Publication> list = service.getAllItems();

        return new ResponseEntity<List<Publication>>(list, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Publication> getPublicationById(@PathVariable("id") int id) {
        Publication entity = service.getPublicationById(id);

        return new ResponseEntity<Publication>(entity, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<Publication>> getPublicationsAllowedUserById(@PathVariable("id") int id) {
        List<Publication> entity = service.getAllPublicationByUserAllowed(id);

        return new ResponseEntity<List<Publication>>(entity, new HttpHeaders(), HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<Object> createPublication(@Valid @RequestBody Publication myItem) {

        Cloud cloud = Cloud.getINSTANCE();
        try {
            User u = userService.getUserById(myItem.getUser().getId());
            myItem.setUser(u);
            myItem.getImages().forEach(image -> {
                image.setPublication(myItem);
                image.setUrl(cloud.uploadImg(image.getUrl()));
            });
            myItem.getVideos().forEach(video -> {
                video.setPublication(myItem);
                video.setUrl(cloud.uploadVideo(video.getUrl()));
            });
            myItem.getAudios().forEach(audio -> {
                audio.setPublication(myItem);
                audio.setUrl(cloud.uploadVideo(audio.getUrl()));
            });
            Publication created = service.createPublication(myItem);
            return new ResponseEntity<Object>(created, new HttpHeaders(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<Object>(-1, new HttpHeaders(), HttpStatus.LOCKED);
        }

    }

    @DeleteMapping("/{id}")
    public HttpStatus deletePublicationById(@PathVariable("id") int id) {
        service.deleteItemById(id);
        return HttpStatus.ACCEPTED;
    }

    @PutMapping("/{id}")
    public ResponseEntity<Publication> UpdatePublication(@Valid @RequestBody Publication myItem, @PathVariable("id") int id) {
        myItem.setId(id);
        Pattern pattern = Pattern.compile("^https://res.cloudinary.com*");
        Cloud cloud = Cloud.getINSTANCE();

        User u = userService.getUserById(myItem.getUser().getId());
        myItem.setUser(u);
        myItem.getImages().forEach(image -> {
            image.setPublication(myItem);
            if (!pattern.matcher(image.getUrl()).find()) {
                image.setUrl(cloud.uploadImg(image.getUrl()));
            }
        });
        myItem.getVideos().forEach(video -> {
            video.setPublication(myItem);
            if (!pattern.matcher(video.getUrl()).find()) {
                video.setUrl(cloud.uploadVideo(video.getUrl()));
            }
        });
        myItem.getAudios().forEach(audio -> {
            audio.setPublication(myItem);
            if (!pattern.matcher(audio.getUrl()).find()) {
                audio.setUrl(cloud.uploadVideo(audio.getUrl()));
            }
        });
        Publication updated = service.updatePublication(myItem);
        return new ResponseEntity<Publication>(updated, new HttpHeaders(), HttpStatus.OK);
    }


    @GetMapping("/{id1}/user/{id2}")
    public ResponseEntity<List<Publication>> getAllPublicationByUserAllowed(@PathVariable("id1")Integer iduser, @PathVariable("id2")Integer idsearch) {
        List<Publication> list = service.getAllPublicationByIdUserByUserAllowed(iduser, idsearch);

        return new ResponseEntity<List<Publication>>(list, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/{id1}/{id2}")
    public ResponseEntity<List<Publication>> getAllPublicationByIdAllowed(@PathVariable("id1")Integer iduser, @PathVariable("id2")Integer idsearch) {
        List<Publication> list = service.getAllPublicationByIdAllowed(iduser, idsearch);

        return new ResponseEntity<List<Publication>>(list, new HttpHeaders(), HttpStatus.OK);
    }


    @PostMapping("like/{id1}/{id2}")
    public ResponseEntity<Integer> addLike(@PathVariable("id1") Integer id1, @PathVariable("id2") Integer id2) {
        try {
            Integer i = service.addLike(id1, id2);
            return new ResponseEntity<Integer>(i, new HttpHeaders(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<Integer>(-1, new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }

    }

    @DeleteMapping("like/{id1}/{id2}")
    public ResponseEntity<Integer> removeLike(@PathVariable("id1") Integer id1, @PathVariable("id2") Integer id2) {
        try {
            Integer i = service.removeLike(id1, id2);
            return new ResponseEntity<Integer>(i, new HttpHeaders(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<Integer>(-1, new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }

    }
}
