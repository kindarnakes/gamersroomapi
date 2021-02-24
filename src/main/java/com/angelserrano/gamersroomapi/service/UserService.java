package com.angelserrano.gamersroomapi.service;

import com.angelserrano.gamersroomapi.Utils.Cloud;
import com.angelserrano.gamersroomapi.dao.UserRepo;
import com.angelserrano.gamersroomapi.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepo repository;


    public List<User> getAllItems() {
        List<User> itemList = repository.findAll();

        if (itemList.size() > 0) {
            return itemList;
        } else {
            return new ArrayList<>();
        }
    }


    public User getUserById(int id) {
        Optional<User> item = repository.findById(id);

        if (item.isPresent()) {
            item.get().setPass("");
            return item.get();
        } else {
            return new User();
        }
    }

    public User createUser(User entity) {
        entity = repository.save(entity);
        return entity;
    }

    public User UpdateItem(User entity) {

        if (entity.getEmail() != null) {
            Optional<User> item = repository.findById(entity.getId());

            if (item.isPresent()) {
                User newEntity = item.get();
                if(!entity.getPortrait().matches(newEntity.getPortrait())){
                    Cloud cloud = Cloud.getINSTANCE();
                    cloud.destroy(newEntity.getPortrait());
                }
                if (entity.getPortrait() != null)
                    newEntity.setPortrait(entity.getPortrait());

                newEntity.setPrivacy(entity.getPrivacy());

                if (entity.getEmail() != null)
                    newEntity.setEmail(entity.getEmail());

                if (entity.getUsername() != null)
                    newEntity.setUsername(entity.getUsername());

                if (entity.getDescription() != null)
                    newEntity.setDescription(entity.getDescription());

                if (entity.accessPass() != null && !entity.accessPass().matches(""))
                    newEntity.setPass(entity.accessPass());

                newEntity = repository.save(newEntity);

                return newEntity;
            } else {
                return new User();
            }
        } else {
            return new User();
        }


    }

    public void deleteItemById(int id) {
        Optional<User> item = repository.findById(id);

        if (item.isPresent()) {
            repository.deleteById(id);
        } else {
        }
    }

    public Integer addFriend(int id1, int id2) {
        return repository.addFriend(id1, id2);
    }

    public Integer removeFriend(int id1, int id2) {
        return repository.removeFriend(id1, id2);
    }

    public Boolean isFriend(int id1, int id2) {
        return repository.isFriend(id1, id2);
    }

    public Boolean mutualFriend(int id1, int id2) {
        return repository.mutualFriends(id1, id2);
    }

    public List<User> getAllowAccess(int id) {
        List<User> list = repository.getUsersAllowAccess(id);

        list.forEach(user -> {
            user.setPass("");
        });
        return list;
    }

    public User getAllowAccessById(int id1, int id2) {
        return repository.getUserAllowAccess(id1, id2);
    }

    public List<User> getAllPetitions(int id) {
        List<User> list = repository.getAllPetitions(id);

        list.forEach(user -> {
            user.setPass("");
        });
        return list;
    }

    public List<User> getAllFriends(int id) {
        List<User> list = repository.getAllFriends(id);
        list.forEach(user -> {
            user.setPass("");
        });
        return list;
    }

    public List<User> getAllFriends(int id, int page, int nperpage) {
        List<User> list = repository.getAllFriends(id, page>0?(page-1)*nperpage:0, nperpage>0?nperpage:1);
        return list;
    }

    public List<User> getUsersAllowAccessByName(String name, int page, int nperpage) {
        List<User> list = repository.getUsersAllowAccessByName(name, page>0?(page-1)*nperpage:0, nperpage>0?nperpage:1);

            return list;
    }
    public List<User> getAllPetitions(int id, int page, int nperpage) {
        List<User> list = repository.getAllPetitions(id, page>0?(page-1)*nperpage:0, nperpage>0?nperpage:1);
            return list;
    }

    public User logIn(String email, String pass) {
        User u = repository.logIn(email, pass);
        return u;
    }

    public List<User> getUsersAllowAccessByName(String name) {
        return repository.getUsersAllowAccessByName(name.toLowerCase(Locale.ROOT));
    }

    public User getUsersAllowAccessByEmail(String email) {
        return repository.getUsersAllowAccessByEmail(email);
    }


}
