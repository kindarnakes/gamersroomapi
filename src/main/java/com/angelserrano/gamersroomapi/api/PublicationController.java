package com.angelserrano.gamersroomapi.api;

import com.angelserrano.gamersroomapi.Utils.Cloud;
import com.angelserrano.gamersroomapi.dao.SecurityRepo;
import com.angelserrano.gamersroomapi.model.Publication;
import com.angelserrano.gamersroomapi.model.SecurityToken;
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
import java.util.Optional;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/publication")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PublicationController {
    @Autowired
    PublicationService service;
    @Autowired
    UserService userService;
    @Autowired
    SecurityRepo securityRepo;

    @GetMapping
    public ResponseEntity<List<Publication>> getAllItems(@RequestHeader(name="key", required = false) String key) {
        Optional<SecurityToken> security = securityRepo.findById(key!=null?key:"");
        if(security.isPresent()) {
            SecurityToken.SECURITY_LEVEL securityLevel = securityRepo.findById(key).get().getSecurity_level();
            if (securityLevel == SecurityToken.SECURITY_LEVEL.ADMIN) {

                List<Publication> list = service.getAllItems();

                return new ResponseEntity<List<Publication>>(list, new HttpHeaders(), HttpStatus.OK);
            } else {
                return new ResponseEntity<List<Publication>>(null, new HttpHeaders(), HttpStatus.FORBIDDEN);
            }
        }else{
            return new ResponseEntity<List<Publication>>(null, new HttpHeaders(), HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Publication> getPublicationById(@PathVariable("id") int id, @RequestHeader(name="key", required = false) String key) {

        Optional<SecurityToken> security = securityRepo.findById(key!=null?key:"");
        if(security.isPresent()) {
            SecurityToken.SECURITY_LEVEL securityLevel = securityRepo.findById(key).get().getSecurity_level();
            if (securityLevel == SecurityToken.SECURITY_LEVEL.ADMIN) {
                Publication entity = service.getPublicationById(id);

                return new ResponseEntity<Publication>(entity, new HttpHeaders(), HttpStatus.OK);
            } else {
                return new ResponseEntity<Publication>(null, new HttpHeaders(), HttpStatus.FORBIDDEN);
            }
        }else{
            return new ResponseEntity<Publication>(null, new HttpHeaders(), HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<Publication>> getPublicationsAllowedUserById(@PathVariable("id") int id, @RequestHeader(name="key", required = false) String key) {
        Optional<SecurityToken> security = securityRepo.findById(key!=null?key:"");
        if(security.isPresent()) {
            SecurityToken.SECURITY_LEVEL securityLevel = securityRepo.findById(key).get().getSecurity_level();
            if (securityLevel == SecurityToken.SECURITY_LEVEL.USER || securityLevel == SecurityToken.SECURITY_LEVEL.ADMIN) {

                List<Publication> entity = service.getAllPublicationByUserAllowed(id);

                return new ResponseEntity<List<Publication>>(entity, new HttpHeaders(), HttpStatus.OK);
            } else {
                return new ResponseEntity<List<Publication>>(null, new HttpHeaders(), HttpStatus.FORBIDDEN);
            }
        }else{
            return new ResponseEntity<List<Publication>>(null, new HttpHeaders(), HttpStatus.FORBIDDEN);
        }
    }


    @PostMapping
    public ResponseEntity<Publication> createPublication(@Valid @RequestBody Publication myItem, @RequestHeader(name="key", required = false) String key) {

        Optional<SecurityToken> security = securityRepo.findById(key!=null?key:"");
        if(security.isPresent()) {
            SecurityToken.SECURITY_LEVEL securityLevel = securityRepo.findById(key).get().getSecurity_level();
            if (securityLevel == SecurityToken.SECURITY_LEVEL.USER || securityLevel == SecurityToken.SECURITY_LEVEL.ADMIN) {

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
                    return new ResponseEntity<Publication>(created, new HttpHeaders(), HttpStatus.OK);
                } catch (Exception e) {
                    e.printStackTrace();
                    return new ResponseEntity<Publication>(null, new HttpHeaders(), HttpStatus.LOCKED);
                }
            } else {
                return new ResponseEntity<Publication>(null, new HttpHeaders(), HttpStatus.FORBIDDEN);
            }
        }else{
            return new ResponseEntity<Publication>(null, new HttpHeaders(), HttpStatus.FORBIDDEN);
        }

    }

    @DeleteMapping("/{id}")
    public HttpStatus deletePublicationById(@PathVariable("id") int id, @RequestHeader(name="key", required = false) String key) {

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

    @PutMapping("/{id}")
    public ResponseEntity<Publication> UpdatePublication(@Valid @RequestBody Publication myItem, @PathVariable("id") int id, @RequestHeader(name="key", required = false) String key) {

        Optional<SecurityToken> security = securityRepo.findById(key!=null?key:"");
        if(security.isPresent()) {
            SecurityToken.SECURITY_LEVEL securityLevel = securityRepo.findById(key).get().getSecurity_level();
            if (securityLevel == SecurityToken.SECURITY_LEVEL.USER || securityLevel == SecurityToken.SECURITY_LEVEL.ADMIN) {

                myItem.setId(id);
                Pattern pattern = Pattern.compile("^http://res.cloudinary.com*");
                Cloud cloud = Cloud.getINSTANCE();

                User u = userService.getUserById(myItem.getUser().getId());
                myItem.setUser(u);
                myItem.getImages().forEach(image -> {
                    if (!pattern.matcher(image.getUrl()).find()) {
                        image.setPublication(myItem);
                        image.setUrl(cloud.uploadImg(image.getUrl()));
                    }
                });
                myItem.getVideos().forEach(video -> {
                    if (!pattern.matcher(video.getUrl()).find()) {
                        video.setPublication(myItem);
                        video.setUrl(cloud.uploadVideo(video.getUrl()));
                    }
                });
                myItem.getAudios().forEach(audio -> {
                    if (!pattern.matcher(audio.getUrl()).find()) {
                        audio.setPublication(myItem);
                        audio.setUrl(cloud.uploadVideo(audio.getUrl()));
                    }
                });
                Publication updated = service.updatePublication(myItem);
                return new ResponseEntity<Publication>(updated, new HttpHeaders(), HttpStatus.OK);
            } else {
                return new ResponseEntity<Publication>(null, new HttpHeaders(), HttpStatus.FORBIDDEN);
            }
        }else{
            return new ResponseEntity<Publication>(null, new HttpHeaders(), HttpStatus.FORBIDDEN);
        }

    }


    @GetMapping("/{id1}/user/{id2}")
    public ResponseEntity<List<Publication>> getAllPublicationByUserAllowed(@PathVariable("id1")Integer iduser, @PathVariable("id2")Integer idsearch, @RequestHeader(name="key", required = false) String key) {

        Optional<SecurityToken> security = securityRepo.findById(key!=null?key:"");
        if(security.isPresent()) {
            SecurityToken.SECURITY_LEVEL securityLevel = securityRepo.findById(key).get().getSecurity_level();
            if (securityLevel == SecurityToken.SECURITY_LEVEL.USER || securityLevel == SecurityToken.SECURITY_LEVEL.ADMIN) {

                List<Publication> list = service.getAllPublicationByIdUserByUserAllowed(iduser, idsearch);

                return new ResponseEntity<List<Publication>>(list, new HttpHeaders(), HttpStatus.OK);
            } else {
                return new ResponseEntity<List<Publication>>(null, new HttpHeaders(), HttpStatus.FORBIDDEN);
            }
        }else{
            return new ResponseEntity<List<Publication>>(null, new HttpHeaders(), HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/{id1}/{id2}")
    public ResponseEntity<Publication> getAllPublicationByIdAllowed(@PathVariable("id1")Integer iduser, @PathVariable("id2")Integer idsearch, @RequestHeader(name="key", required = false) String key) {

        Optional<SecurityToken> security = securityRepo.findById(key!=null?key:"");
        if(security.isPresent()) {
            SecurityToken.SECURITY_LEVEL securityLevel = securityRepo.findById(key).get().getSecurity_level();
            if (securityLevel == SecurityToken.SECURITY_LEVEL.USER || securityLevel == SecurityToken.SECURITY_LEVEL.ADMIN) {

                Publication list = service.getAllPublicationByIdAllowed(iduser, idsearch);

                return new ResponseEntity<Publication>(list, new HttpHeaders(), HttpStatus.OK);
            } else {
                return new ResponseEntity<Publication>(null, new HttpHeaders(), HttpStatus.FORBIDDEN);
            }
        }else{
            return new ResponseEntity<Publication>(null, new HttpHeaders(), HttpStatus.FORBIDDEN);
        }

    }


    @PostMapping("like/{id1}/{id2}")
    public ResponseEntity<Integer> addLike(@PathVariable("id1") Integer id1, @PathVariable("id2") Integer id2, @RequestHeader(name="key", required = false) String key) {


        Optional<SecurityToken> security = securityRepo.findById(key!=null?key:"");
        if(security.isPresent()) {
            SecurityToken.SECURITY_LEVEL securityLevel = securityRepo.findById(key).get().getSecurity_level();
            if (securityLevel == SecurityToken.SECURITY_LEVEL.USER || securityLevel == SecurityToken.SECURITY_LEVEL.ADMIN) {

                try {
                    Integer i = service.addLike(id1, id2);
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

    @DeleteMapping("like/{id1}/{id2}")
    public ResponseEntity<Integer> removeLike(@PathVariable("id1") Integer id1, @PathVariable("id2") Integer id2, @RequestHeader(name="key", required = false) String key) {


        Optional<SecurityToken> security = securityRepo.findById(key!=null?key:"");
        if(security.isPresent()) {
            SecurityToken.SECURITY_LEVEL securityLevel = securityRepo.findById(key).get().getSecurity_level();
            if (securityLevel == SecurityToken.SECURITY_LEVEL.USER || securityLevel == SecurityToken.SECURITY_LEVEL.ADMIN) {
                try {
                    Integer i = service.removeLike(id1, id2);
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



    @GetMapping("/{id1}/user/{id2}/{page}/{nperpage}")
    public ResponseEntity<List<Publication>> getAllPublicationByUserAllowed(@PathVariable("id1")Integer iduser, @PathVariable("id2")Integer idsearch, @PathVariable("page") int page, @PathVariable("nperpage") int nperpage, @RequestHeader(name="key", required = false) String key) {

        Optional<SecurityToken> security = securityRepo.findById(key!=null?key:"");
        if(security.isPresent()) {
            SecurityToken.SECURITY_LEVEL securityLevel = securityRepo.findById(key).get().getSecurity_level();
            if (securityLevel == SecurityToken.SECURITY_LEVEL.USER || securityLevel == SecurityToken.SECURITY_LEVEL.ADMIN) {

                List<Publication> list = service.getAllPublicationByIdUserByUserAllowed(iduser, idsearch, page, nperpage);

                return new ResponseEntity<List<Publication>>(list, new HttpHeaders(), HttpStatus.OK);
            } else {
                return new ResponseEntity<List<Publication>>(null, new HttpHeaders(), HttpStatus.FORBIDDEN);
            }
        }else{
            return new ResponseEntity<List<Publication>>(null, new HttpHeaders(), HttpStatus.FORBIDDEN);
        }
    }


    @GetMapping("/user/{id}/{page}/{nperpage}")
    public ResponseEntity<List<Publication>> getPublicationsAllowedUserById(@PathVariable("id") int id, @PathVariable("page") int page, @PathVariable("nperpage") int nperpage, @RequestHeader(name="key", required = false) String key) {



        Optional<SecurityToken> security = securityRepo.findById(key!=null?key:"");
        if(security.isPresent()) {
            SecurityToken.SECURITY_LEVEL securityLevel = securityRepo.findById(key).get().getSecurity_level();
            if (securityLevel == SecurityToken.SECURITY_LEVEL.USER || securityLevel == SecurityToken.SECURITY_LEVEL.ADMIN) {

                List<Publication> entity = service.getAllPublicationByUserAllowed(id, page, nperpage);

                return new ResponseEntity<List<Publication>>(entity, new HttpHeaders(), HttpStatus.OK);
            } else {
                return new ResponseEntity<List<Publication>>(null, new HttpHeaders(), HttpStatus.FORBIDDEN);
            }
        }else{
            return new ResponseEntity<List<Publication>>(null, new HttpHeaders(), HttpStatus.FORBIDDEN);
        }
    }
}
