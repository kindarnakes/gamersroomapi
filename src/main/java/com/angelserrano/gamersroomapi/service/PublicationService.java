package com.angelserrano.gamersroomapi.service;

import com.angelserrano.gamersroomapi.Utils.Cloud;
import com.angelserrano.gamersroomapi.dao.*;
import com.angelserrano.gamersroomapi.model.*;
import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class PublicationService {

    @Autowired
    PublicationRepo repository;
    @Autowired
    CoordinatesRepo coordinatesRepo;
    @Autowired
    ImageRepo imageRepo;
    @Autowired
    AudioRepo audioRepo;
    @Autowired
    VideoRepo videoRepo;
    Cloud cloud;

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
        cloud = Cloud.getINSTANCE();

        if(item.isPresent()){
            Publication p = item.get();

            p.setUser(publication.getUser());
            if(publication.getCoordinates() == null && p.getCoordinates() != null){
                Coordinates coord = coordinatesRepo.findById(p.getCoordinates().getId()).get();
                p.setCoordinates(null);
                coordinatesRepo.delete(coord);
            }else if(publication.getCoordinates() != null && p.getCoordinates() != null){
                Coordinates coord = coordinatesRepo.findById(p.getCoordinates().getId()).get();
                coord.setLatitude(publication.getCoordinates().getLatitude());
                coord.setLongitude(publication.getCoordinates().getLongitude());
                coordinatesRepo.save(coord);
            }else{
                p.setCoordinates(publication.getCoordinates());
            }


            p.setAudios(publication.getAudios());
            List<Audio> audios = p.getAudios();
            audios.forEach(audio -> {
                if (audio.getPublication() == null){
                    cloud.destroy(audio.getUrl());
                    audioRepo.delete(audioRepo.getOne(audio.getId()));
                }
            });

            p.setVideos(publication.getVideos());
            List<Video> videos = p.getVideos();
            videos.forEach(video -> {
                if (video.getPublication() == null){
                    cloud.destroy(video.getUrl());
                    videoRepo.delete(videoRepo.getOne(video.getId()));
                }
            });


            List<Image> images = p.getImages();
            p.setImages(publication.getImages());
            images.forEach(image -> {
                if (image.getPublication() == null){
                    cloud.destroy(image.getUrl());
                    imageRepo.delete(imageRepo.getOne(image.getId()));
                }
            });

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


    public List<Publication> getAllPublicationByIdUserByUserAllowed(Integer iduser , Integer idsearch, int page, int nperpage) {
        List<Publication> itemList = repository.getPublicationsByIdUserAllowAccessByIdUser(iduser, idsearch, page>0?(page-1)*nperpage:0, nperpage>0?nperpage:1);
        if (itemList.size() > 0) {
            return itemList;
        } else {
            return new ArrayList<>();
        }
    }

    public List<Publication> getAllPublicationByUserAllowed(Integer iduser, int page, int nperpage) {
        List<Publication> itemList = repository.getPublicationsAllowAccessByIdUser(iduser, page>0?(page-1)*nperpage:0, nperpage>0?nperpage:1);
        if (itemList.size() > 0) {
            return itemList;
        } else {
            return new ArrayList<>();
        }
    }



    public Publication getAllPublicationByIdAllowed(Integer iduser , Integer idsearch) {
        Publication itemList = repository.getPublicationsByIdAllowAccessByIdUser(iduser, idsearch);
            return itemList;
    }


    public Integer addLike(Integer iduser, Integer idpublication){
        return repository.addLike(iduser,idpublication);
    }

    public Integer removeLike(Integer iduser, Integer idpublication){
        return repository.removeLike(iduser,idpublication);
    }
}
