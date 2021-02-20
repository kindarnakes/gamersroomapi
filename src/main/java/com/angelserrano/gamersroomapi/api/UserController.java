package com.angelserrano.gamersroomapi.api;

import com.angelserrano.gamersroomapi.Utils.Cloud;
import com.angelserrano.gamersroomapi.model.User;
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
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService service;



    @GetMapping
    public ResponseEntity<List<User>> getAllItems() {
        List<User> list = service.getAllItems();

        return new ResponseEntity<List<User>>(list, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/allow/{id}")
    public ResponseEntity<List<User>> getAllAllowAccess(@PathVariable("id") int id) {
        List<User> list = service.getAllowAccess(id);
        return new ResponseEntity<List<User>>(list, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/petitions/{id}")
    public ResponseEntity<List<User>> getAllPetitions(@PathVariable("id") int id) {
        List<User> list = service.getAllPetitions(id);
        return new ResponseEntity<List<User>>(list, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/petitions/{id}/{page}/{nperpage}")
    public ResponseEntity<List<User>> getAllPetitionsPaged(@PathVariable("id") int id, @PathVariable("page") int page, @PathVariable("nperpage") int nperpage) {
        List<User> list = service.getAllPetitions(id, page, nperpage);
        return new ResponseEntity<List<User>>(list, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/friends/{id}")
    public ResponseEntity<List<User>> getAllFriends(@PathVariable("id") int id) {
        List<User> list = service.getAllFriends(id);
        return new ResponseEntity<List<User>>(list, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/friends/{id}/{page}/{nperpage}")
    public ResponseEntity<List<User>> getAllFriendsPaged(@PathVariable("id") int id, @PathVariable("page") int page, @PathVariable("nperpage") int nperpage) {
        List<User> list = service.getAllFriends(id, page, nperpage);
        return new ResponseEntity<List<User>>(list, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/allow/{id1}/{id2}")
    public ResponseEntity<Object> getAllAllowAccess(@PathVariable("id1") int id1, @PathVariable("id2") int id2) {
        User u = service.getAllowAccessById(id1, id2);
        if (u != null) {
            return new ResponseEntity<Object>(u, new HttpHeaders(), HttpStatus.OK);
        }else{
            return new ResponseEntity<Object>(-1, new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") int id) {
        User entity = service.getUserById(id);

        return new ResponseEntity<User>(entity, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<User>> getAllByName(@PathVariable("name") String name) {
        List<User> list = service.getUsersAllowAccessByName(name);

        return new ResponseEntity<List<User>>(list, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<User> getAllByEmail(@PathVariable("email") String email) {
        User user = service.getUsersAllowAccessByEmail(email);

        return new ResponseEntity<User>(user, new HttpHeaders(), HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<Object> createUser(@Valid @RequestBody User myItem) {
        if(myItem.getPortrait()!=null){
            myItem.setPortrait(Cloud.getINSTANCE().uploadPortrait(myItem.getPortrait()));
        }
        if (myItem.getUsername() == null) {
            String username = myItem.getEmail();
            Integer i = username.indexOf("@");
            if(i > 0 && i < username.length()) {
                username = username.substring(0, i);
                myItem.setUsername(username);
            }
        }
        try {
            User created = service.createUser(myItem);
            return new ResponseEntity<Object>(created, new HttpHeaders(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<Object>(-1, new HttpHeaders(), HttpStatus.LOCKED);
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity<User> UpdateUser(@Valid @RequestBody User myItem, @PathVariable("id") int id) {
        myItem.setId(id);
        Pattern pattern = Pattern.compile("^https://res.cloudinary.com*");
        if (!pattern.matcher(myItem.getPortrait()).find()) {
            myItem.setPortrait(Cloud.getINSTANCE().uploadPortrait(myItem.getPortrait()));
        }
        User updated = service.UpdateItem(myItem);
        return new ResponseEntity<User>(updated, new HttpHeaders(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public HttpStatus deleteUserById(@PathVariable("id") int id) {
        service.deleteItemById(id);
        return HttpStatus.ACCEPTED;
    }

    @PostMapping("friend/{id1}/{id2}")
    public ResponseEntity<Integer> addFriend(@PathVariable("id1") Integer id1, @PathVariable("id2") Integer id2) {
        try {
            Integer i = service.addFriend(id1, id2);
            return new ResponseEntity<Integer>(i, new HttpHeaders(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<Integer>(-1, new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }

    }

    @DeleteMapping("friend/{id1}/{id2}")
    public ResponseEntity<Integer> removeFriend(@PathVariable("id1") Integer id1, @PathVariable("id2") Integer id2) {
        try {
            Integer i = service.removeFriend(id1, id2);
            return new ResponseEntity<Integer>(i, new HttpHeaders(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<Integer>(-1, new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("friend/{id1}/{id2}")
    public ResponseEntity<Boolean> isFriend(@PathVariable("id1") Integer id1, @PathVariable("id2") Integer id2) {
        try {
            Boolean i = service.isFriend(id1, id2);
            if(i == null){
                i = false;
            }
            return new ResponseEntity<Boolean>(i, new HttpHeaders(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<Boolean>(false, new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }

    }
    @GetMapping("mutualfriend/{id1}/{id2}")
    public ResponseEntity<Boolean> mutualFriend(@PathVariable("id1") Integer id1, @PathVariable("id2") Integer id2) {
        try {
            Boolean i = service.mutualFriend(id1, id2);
            if(i == null){
                i = false;
            }
            return new ResponseEntity<Boolean>(i, new HttpHeaders(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<Boolean>(false, new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }

    }


    @PostMapping("/login")
    public ResponseEntity<User> logIn(@RequestBody User u){
        User user = service.logIn(u.getEmail(), u.accessPass());
        return new ResponseEntity<User>(user, new HttpHeaders(), HttpStatus.OK);
    }



}
