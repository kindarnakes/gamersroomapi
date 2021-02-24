package com.angelserrano.gamersroomapi.api;

import com.angelserrano.gamersroomapi.Utils.Cloud;
import com.angelserrano.gamersroomapi.dao.SecurityRepo;
import com.angelserrano.gamersroomapi.model.SecurityToken;
import com.angelserrano.gamersroomapi.model.User;
import com.angelserrano.gamersroomapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {
    @Autowired
    UserService service;
    @Autowired
    SecurityRepo securityRepo;



    @GetMapping
    public ResponseEntity<List<User>> getAllItems(@RequestHeader(name="key", required = false) String key) {
        Optional<SecurityToken> security = securityRepo.findById(key!=null?key:"");
        if(security.isPresent()) {
            SecurityToken.SECURITY_LEVEL securityLevel = securityRepo.findById(key).get().getSecurity_level();
            if (securityLevel == SecurityToken.SECURITY_LEVEL.ADMIN) {
                List<User> list = service.getAllItems();

                return new ResponseEntity<List<User>>(list, new HttpHeaders(), HttpStatus.OK);
            } else {
                return new ResponseEntity<List<User>>(null, new HttpHeaders(), HttpStatus.FORBIDDEN);
            }
        }else{
            return new ResponseEntity<List<User>>(null, new HttpHeaders(), HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/allow/{id}")
    public ResponseEntity<List<User>> getAllAllowAccess(@PathVariable("id") int id, @RequestHeader(name="key", required = false) String key) {

        Optional<SecurityToken> security = securityRepo.findById(key!=null?key:"");
        if(security.isPresent()) {
            SecurityToken.SECURITY_LEVEL securityLevel = securityRepo.findById(key).get().getSecurity_level();
            if (securityLevel == SecurityToken.SECURITY_LEVEL.USER || securityLevel == SecurityToken.SECURITY_LEVEL.ADMIN) {
                List<User> list = service.getAllowAccess(id);
                return new ResponseEntity<List<User>>(list, new HttpHeaders(), HttpStatus.OK);
            } else {
                return new ResponseEntity<List<User>>(null, new HttpHeaders(), HttpStatus.FORBIDDEN);
            }
        }else{
            return new ResponseEntity<List<User>>(null, new HttpHeaders(), HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/petitions/{id}")
    public ResponseEntity<List<User>> getAllPetitions(@PathVariable("id") int id, @RequestHeader(name="key", required = false) String key) {

        Optional<SecurityToken> security = securityRepo.findById(key!=null?key:"");
        if(security.isPresent()) {
            SecurityToken.SECURITY_LEVEL securityLevel = securityRepo.findById(key).get().getSecurity_level();
            if (securityLevel == SecurityToken.SECURITY_LEVEL.USER ||securityLevel == SecurityToken.SECURITY_LEVEL.ADMIN) {
                List<User> list = service.getAllPetitions(id);
                return new ResponseEntity<List<User>>(list, new HttpHeaders(), HttpStatus.OK);
            } else {
                return new ResponseEntity<List<User>>(null, new HttpHeaders(), HttpStatus.FORBIDDEN);
            }
        }else{
            return new ResponseEntity<List<User>>(null, new HttpHeaders(), HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/petitions/{id}/{page}/{nperpage}")
    public ResponseEntity<List<User>> getAllPetitionsPaged(@PathVariable("id") int id, @PathVariable("page") int page, @PathVariable("nperpage") int nperpage, @RequestHeader(name="key", required = false) String key) {

        Optional<SecurityToken> security = securityRepo.findById(key!=null?key:"");
        if(security.isPresent()) {
            SecurityToken.SECURITY_LEVEL securityLevel = securityRepo.findById(key).get().getSecurity_level();
            if (securityLevel == SecurityToken.SECURITY_LEVEL.USER || securityLevel == SecurityToken.SECURITY_LEVEL.ADMIN) {
                List<User> list = service.getAllPetitions(id, page, nperpage);
                return new ResponseEntity<List<User>>(list, new HttpHeaders(), HttpStatus.OK);
            } else {
                return new ResponseEntity<List<User>>(null, new HttpHeaders(), HttpStatus.FORBIDDEN);
            }
        }else{
            return new ResponseEntity<List<User>>(null, new HttpHeaders(), HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/friends/{id}")
    public ResponseEntity<List<User>> getAllFriends(@PathVariable("id") int id, @RequestHeader(name="key", required = false) String key) {


        Optional<SecurityToken> security = securityRepo.findById(key!=null?key:"");
        if(security.isPresent()) {
            SecurityToken.SECURITY_LEVEL securityLevel = securityRepo.findById(key).get().getSecurity_level();
            if (securityLevel == SecurityToken.SECURITY_LEVEL.USER || securityLevel == SecurityToken.SECURITY_LEVEL.ADMIN) {
                List<User> list = service.getAllFriends(id);
                return new ResponseEntity<List<User>>(list, new HttpHeaders(), HttpStatus.OK);
            } else {
                return new ResponseEntity<List<User>>(null, new HttpHeaders(), HttpStatus.FORBIDDEN);
            }
        }else{
            return new ResponseEntity<List<User>>(null, new HttpHeaders(), HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/friends/{id}/{page}/{nperpage}")
    public ResponseEntity<List<User>> getAllFriendsPaged(@PathVariable("id") int id, @PathVariable("page") int page, @PathVariable("nperpage") int nperpage, @RequestHeader(name="key", required = false) String key) {

        Optional<SecurityToken> security = securityRepo.findById(key!=null?key:"");
        if(security.isPresent()) {
            SecurityToken.SECURITY_LEVEL securityLevel = securityRepo.findById(key).get().getSecurity_level();
            if (securityLevel == SecurityToken.SECURITY_LEVEL.USER || securityLevel == SecurityToken.SECURITY_LEVEL.ADMIN) {
                List<User> list = service.getAllFriends(id, page, nperpage);
                return new ResponseEntity<List<User>>(list, new HttpHeaders(), HttpStatus.OK);
            } else {
                return new ResponseEntity<List<User>>(null, new HttpHeaders(), HttpStatus.FORBIDDEN);
            }
        }else{
            return new ResponseEntity<List<User>>(null, new HttpHeaders(), HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/allow/{id1}/{id2}")
    public ResponseEntity<Object> getAllAllowAccess(@PathVariable("id1") int id1, @PathVariable("id2") int id2, @RequestHeader(name="key", required = false) String key) {

        Optional<SecurityToken> security = securityRepo.findById(key!=null?key:"");
        if(security.isPresent()) {
            SecurityToken.SECURITY_LEVEL securityLevel = securityRepo.findById(key).get().getSecurity_level();
            if (securityLevel == SecurityToken.SECURITY_LEVEL.USER || securityLevel == SecurityToken.SECURITY_LEVEL.ADMIN) {

                User u = service.getAllowAccessById(id1, id2);
                if (u != null) {
                    return new ResponseEntity<Object>(u, new HttpHeaders(), HttpStatus.OK);
                }else{
                    return new ResponseEntity<Object>(-1, new HttpHeaders(), HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity<Object>(null, new HttpHeaders(), HttpStatus.FORBIDDEN);
            }
        }else{
            return new ResponseEntity<Object>(null, new HttpHeaders(), HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") int id, @RequestHeader(name="key", required = false) String key) {

        Optional<SecurityToken> security = securityRepo.findById(key!=null?key:"");
        if(security.isPresent()) {
            SecurityToken.SECURITY_LEVEL securityLevel = securityRepo.findById(key).get().getSecurity_level();
            if (securityLevel == SecurityToken.SECURITY_LEVEL.USER || securityLevel == SecurityToken.SECURITY_LEVEL.ADMIN) {

                User entity = service.getUserById(id);

                return new ResponseEntity<User>(entity, new HttpHeaders(), HttpStatus.OK);
            } else {
                return new ResponseEntity<User>(null, new HttpHeaders(), HttpStatus.FORBIDDEN);
            }
        }else{
            return new ResponseEntity<User>(null, new HttpHeaders(), HttpStatus.FORBIDDEN);
        }

    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<User>> getAllByName(@PathVariable("name") String name, @RequestHeader(name="key", required = false) String key) {


        Optional<SecurityToken> security = securityRepo.findById(key!=null?key:"");
        if(security.isPresent()) {
            SecurityToken.SECURITY_LEVEL securityLevel = securityRepo.findById(key).get().getSecurity_level();
            if (securityLevel == SecurityToken.SECURITY_LEVEL.USER || securityLevel == SecurityToken.SECURITY_LEVEL.ADMIN) {
                List<User> list = service.getUsersAllowAccessByName(name);

                return new ResponseEntity<List<User>>(list, new HttpHeaders(), HttpStatus.OK);
            } else {
                return new ResponseEntity<List<User>>(null, new HttpHeaders(), HttpStatus.FORBIDDEN);
            }
        }else{
            return new ResponseEntity<List<User>>(null, new HttpHeaders(), HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/name/{name}/{page}/{nperpage}")
    public ResponseEntity<List<User>> getAllByName(@PathVariable("name") String name, @PathVariable("page") int page, @PathVariable("nperpage") int nperpage, @RequestHeader(name="key", required = false) String key) {

        Optional<SecurityToken> security = securityRepo.findById(key!=null?key:"");
        if(security.isPresent()) {
            SecurityToken.SECURITY_LEVEL securityLevel = securityRepo.findById(key).get().getSecurity_level();
            if (securityLevel == SecurityToken.SECURITY_LEVEL.USER || securityLevel == SecurityToken.SECURITY_LEVEL.ADMIN) {
                List<User> list = service.getUsersAllowAccessByName(name, page, nperpage);

                return new ResponseEntity<List<User>>(list, new HttpHeaders(), HttpStatus.OK);
            } else {
                return new ResponseEntity<List<User>>(null, new HttpHeaders(), HttpStatus.FORBIDDEN);
            }
        }else{
            return new ResponseEntity<List<User>>(null, new HttpHeaders(), HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<User> getByEmail(@PathVariable("email") String email, @RequestHeader(name="key", required = false) String key) {

        Optional<SecurityToken> security = securityRepo.findById(key!=null?key:"");
        if(security.isPresent()) {
            SecurityToken.SECURITY_LEVEL securityLevel = securityRepo.findById(key).get().getSecurity_level();
            if (securityLevel == SecurityToken.SECURITY_LEVEL.USER || securityLevel == SecurityToken.SECURITY_LEVEL.ADMIN) {
                User user = service.getUsersAllowAccessByEmail(email);

                return new ResponseEntity<User>(user, new HttpHeaders(), HttpStatus.OK);
            } else {
                return new ResponseEntity<User>(null, new HttpHeaders(), HttpStatus.FORBIDDEN);
            }
        }else{
            return new ResponseEntity<User>(null, new HttpHeaders(), HttpStatus.FORBIDDEN);
        }
    }


    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User myItem, @RequestHeader(name="key", required = false) String key) {

        Optional<SecurityToken> security = securityRepo.findById(key!=null?key:"");
        if(security.isPresent()) {
            SecurityToken.SECURITY_LEVEL securityLevel = securityRepo.findById(key).get().getSecurity_level();
            if (securityLevel == SecurityToken.SECURITY_LEVEL.USER || securityLevel == SecurityToken.SECURITY_LEVEL.ADMIN) {


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
                    return new ResponseEntity<>(created, new HttpHeaders(), HttpStatus.OK);
                } catch (Exception e) {
                    return new ResponseEntity<>(null, new HttpHeaders(), HttpStatus.LOCKED);
                }
            } else {
                return new ResponseEntity<>(null, new HttpHeaders(), HttpStatus.FORBIDDEN);
            }
        }else{
            return new ResponseEntity<>(null, new HttpHeaders(), HttpStatus.FORBIDDEN);
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity<User> UpdateUser(@Valid @RequestBody User myItem, @PathVariable("id") int id, @RequestHeader(name="key", required = false) String key) {

        Optional<SecurityToken> security = securityRepo.findById(key!=null?key:"");
        if(security.isPresent()) {
            SecurityToken.SECURITY_LEVEL securityLevel = securityRepo.findById(key).get().getSecurity_level();
            if (securityLevel == SecurityToken.SECURITY_LEVEL.USER || securityLevel == SecurityToken.SECURITY_LEVEL.ADMIN) {

                myItem.setId(id);
                Pattern pattern = Pattern.compile("^https://res.cloudinary.com*");
                if (!pattern.matcher(myItem.getPortrait()).find()) {
                    myItem.setPortrait(Cloud.getINSTANCE().uploadPortrait(myItem.getPortrait()));
                }
                User updated = service.UpdateItem(myItem);
                return new ResponseEntity<User>(updated, new HttpHeaders(), HttpStatus.OK);
            } else {
                return new ResponseEntity<User>(null, new HttpHeaders(), HttpStatus.FORBIDDEN);
            }
        }else{
            return new ResponseEntity<User>(null, new HttpHeaders(), HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/{id}")
    public HttpStatus deleteUserById(@PathVariable("id") int id, @RequestHeader(name="key", required = false) String key) {

        Optional<SecurityToken> security = securityRepo.findById(key!=null?key:"");
        if(security.isPresent()) {
            SecurityToken.SECURITY_LEVEL securityLevel = securityRepo.findById(key).get().getSecurity_level();
            if (securityLevel == SecurityToken.SECURITY_LEVEL.ADMIN) {
                service.deleteItemById(id);
                return HttpStatus.ACCEPTED;
            } else {
                return HttpStatus.FORBIDDEN;
            }
        }else{
            return HttpStatus.FORBIDDEN;
        }
    }

    @PostMapping("friend/{id1}/{id2}")
    public ResponseEntity<Integer> addFriend(@PathVariable("id1") Integer id1, @PathVariable("id2") Integer id2, @RequestHeader(name="key", required = false) String key) {

        Optional<SecurityToken> security = securityRepo.findById(key!=null?key:"");
        if(security.isPresent()) {
            SecurityToken.SECURITY_LEVEL securityLevel = securityRepo.findById(key).get().getSecurity_level();
            if (securityLevel == SecurityToken.SECURITY_LEVEL.USER || securityLevel == SecurityToken.SECURITY_LEVEL.ADMIN) {

                try {
                    Integer i = service.addFriend(id1, id2);
                    return new ResponseEntity<Integer>(i, new HttpHeaders(), HttpStatus.OK);
                } catch (Exception e) {
                    return new ResponseEntity<Integer>(-1, new HttpHeaders(), HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity<Integer>(null, new HttpHeaders(), HttpStatus.FORBIDDEN);
            }
        }else{
            return new ResponseEntity<Integer>(null, new HttpHeaders(), HttpStatus.FORBIDDEN);
        }

    }

    @DeleteMapping("friend/{id1}/{id2}")
    public ResponseEntity<Integer> removeFriend(@PathVariable("id1") Integer id1, @PathVariable("id2") Integer id2, @RequestHeader(name="key", required = false) String key) {

        Optional<SecurityToken> security = securityRepo.findById(key!=null?key:"");
        if(security.isPresent()) {
            SecurityToken.SECURITY_LEVEL securityLevel = securityRepo.findById(key).get().getSecurity_level();
            if (securityLevel == SecurityToken.SECURITY_LEVEL.USER || securityLevel == SecurityToken.SECURITY_LEVEL.ADMIN) {

                try {
                    Integer i = service.removeFriend(id1, id2);
                    return new ResponseEntity<Integer>(i, new HttpHeaders(), HttpStatus.OK);
                } catch (Exception e) {
                    return new ResponseEntity<Integer>(-1, new HttpHeaders(), HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity<Integer>(null, new HttpHeaders(), HttpStatus.FORBIDDEN);
            }
        }else{
            return new ResponseEntity<Integer>(null, new HttpHeaders(), HttpStatus.FORBIDDEN);
        }


    }

    @GetMapping("friend/{id1}/{id2}")
    public ResponseEntity<Boolean> isFriend(@PathVariable("id1") Integer id1, @PathVariable("id2") Integer id2, @RequestHeader(name="key", required = false) String key) {

        Optional<SecurityToken> security = securityRepo.findById(key!=null?key:"");
        if(security.isPresent()) {
            SecurityToken.SECURITY_LEVEL securityLevel = securityRepo.findById(key).get().getSecurity_level();
            if (securityLevel == SecurityToken.SECURITY_LEVEL.USER || securityLevel == SecurityToken.SECURITY_LEVEL.ADMIN) {

                try {
                    Boolean i = service.isFriend(id1, id2);
                    if(i == null){
                        i = false;
                    }
                    return new ResponseEntity<Boolean>(i, new HttpHeaders(), HttpStatus.OK);
                } catch (Exception e) {
                    return new ResponseEntity<Boolean>(false, new HttpHeaders(), HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity<Boolean>(null, new HttpHeaders(), HttpStatus.FORBIDDEN);
            }
        }else{
            return new ResponseEntity<Boolean>(null, new HttpHeaders(), HttpStatus.FORBIDDEN);
        }



    }
    @GetMapping("mutualfriend/{id1}/{id2}")
    public ResponseEntity<Boolean> mutualFriend(@PathVariable("id1") Integer id1, @PathVariable("id2") Integer id2, @RequestHeader(name="key", required = false) String key) {

        Optional<SecurityToken> security = securityRepo.findById(key!=null?key:"");
        if(security.isPresent()) {
            SecurityToken.SECURITY_LEVEL securityLevel = securityRepo.findById(key).get().getSecurity_level();
            if (securityLevel == SecurityToken.SECURITY_LEVEL.USER || securityLevel == SecurityToken.SECURITY_LEVEL.ADMIN) {

                try {
                    Boolean i = service.mutualFriend(id1, id2);
                    if(i == null){
                        i = false;
                    }
                    return new ResponseEntity<Boolean>(i, new HttpHeaders(), HttpStatus.OK);
                } catch (Exception e) {
                    return new ResponseEntity<Boolean>(false, new HttpHeaders(), HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity<Boolean>(null, new HttpHeaders(), HttpStatus.FORBIDDEN);
            }
        }else{
            return new ResponseEntity<Boolean>(null, new HttpHeaders(), HttpStatus.FORBIDDEN);
        }



    }


    @PostMapping("/login")
    public ResponseEntity<User> logIn(@RequestBody User u, @RequestHeader(name="key", required = false) String key){

        Optional<SecurityToken> security = securityRepo.findById(key!=null?key:"");
        if(security.isPresent()) {
            SecurityToken.SECURITY_LEVEL securityLevel = securityRepo.findById(key).get().getSecurity_level();
            if (securityLevel == SecurityToken.SECURITY_LEVEL.USER || securityLevel == SecurityToken.SECURITY_LEVEL.ADMIN) {

                User user = service.logIn(u.getEmail(), u.accessPass());
                return new ResponseEntity<User>(user, new HttpHeaders(), HttpStatus.OK);
            } else {
                return new ResponseEntity<User>(null, new HttpHeaders(), HttpStatus.FORBIDDEN);
            }
        }else{
            return new ResponseEntity<User>(null, new HttpHeaders(), HttpStatus.FORBIDDEN);
        }


    }



}
