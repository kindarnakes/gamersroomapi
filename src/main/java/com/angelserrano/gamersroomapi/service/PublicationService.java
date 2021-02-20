package com.angelserrano.gamersroomapi.service;

import com.angelserrano.gamersroomapi.dao.PublicationRepo;
import com.angelserrano.gamersroomapi.model.Publication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PublicationService {

    @Autowired
    PublicationRepo repository;

    public List<Publication> getAllItems() {
        List<Publication> itemList = repository.findAll();

        if (itemList.size() > 0) {
            return itemList;
        } else {
            return new ArrayList<>();
        }
    }


    public Publication getPublicationById(int id) {
        Optional<Publication> item = repository.findById(id);

        if (item.isPresent()) {
            return item.get();
        } else {
            return new Publication();
        }
    }

    public Publication createPublication(Publication entity) {
        entity = repository.save(entity);
        return entity;
    }

    public void deleteItemById(int id) {
        Optional<Publication> item = repository.findById(id);

        if (item.isPresent()) {
            repository.deleteById(id);
        } else {
        }
    }

    public Publication updatePublication(Publication publication){
        Optional<Publication> item = repository.findById(publication.getId());

        if(item.isPresent()){
            Publication p = item.get();
            p.setUser(publication.getUser());
            p.setCoordinates(p.getCoordinates());
            p.setAudios(publication.getAudios());
            p.setVideos(publication.getVideos());
            p.setImages(publication.getImages());
            p.setUserlikes(publication.getUserlikes());
            p.setComments(publication.getComments());
            p.setText(publication.getText());

            p = repository.save(p);
            return p;
        }
        return new Publication();

    }


    public List<Publication> getAllPublicationByIdUserByUserAllowed(Integer iduser , Integer idsearch) {
        List<Publication> itemList = repository.getPublicationsByIdUserAllowAccessByIdUser(iduser, idsearch);
        if (itemList.size() > 0) {
            return itemList;
        } else {
            return new ArrayList<>();
        }
    }

    public List<Publication> getAllPublicationByUserAllowed(Integer iduser) {
        List<Publication> itemList = repository.getPublicationsAllowAccessByIdUser(iduser);
        if (itemList.size() > 0) {
            return itemList;
        } else {
            return new ArrayList<>();
        }
    }



    public List<Publication> getAllPublicationByIdAllowed(Integer iduser , Integer idsearch) {
        List<Publication> itemList = repository.getPublicationsByIdAllowAccessByIdUser(iduser, idsearch);
        if (itemList.size() > 0) {
            return itemList;
        } else {
            return new ArrayList<>();
        }
    }


    public Integer addLike(Integer iduser, Integer idpublication){
        return repository.addLike(iduser,idpublication);
    }

    public Integer removeLike(Integer iduser, Integer idpublication){
        return repository.removeLike(iduser,idpublication);
    }
}
