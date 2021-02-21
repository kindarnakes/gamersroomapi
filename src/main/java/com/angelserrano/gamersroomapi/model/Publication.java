package com.angelserrano.gamersroomapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "Publication", schema = "public")
@JsonIgnoreProperties(value = { "comments", "userlikes", }, allowSetters = false, allowGetters = true)
public class Publication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_user")
    @JsonIgnoreProperties({"friends", "comments", "publications", "description", "pass", "email", "privacy", "likes"})
    private User user;

    @Column(name = "text")
    private String text;

    @Column(name = "post_date", nullable = false, updatable = false, insertable = true)
    private LocalDateTime time;
    @OneToMany(mappedBy = "publication", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
    @JsonIgnoreProperties("publication")
    private List<Image> images;
    @OneToMany(mappedBy = "publication", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
    @JsonIgnoreProperties("publication")
    private List<Video> videos;
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE}, mappedBy = "publication")
    @JsonIgnoreProperties("publication")
    private List<Audio> audios;
    @OneToOne(mappedBy = "publication", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
    @JsonIgnoreProperties("publication")
    private Coordinates coordinates;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy = "publication")
    private List<Comment> comments;


    @ManyToMany(cascade = {CascadeType.REMOVE}, mappedBy = "likes")
    @JsonIgnoreProperties({"friends", "comments", "publications", "description", "pass", "email", "portrait", "privacy", "username", "likes"})
    private Set<User> userlikes;


    public Publication() {
        this.userlikes = new HashSet<>();
        this.comments = new ArrayList<>();
        this.images = new ArrayList<>();
        this.videos = new ArrayList<>();
        this.audios = new ArrayList<>();
        this.time = LocalDateTime.now();
    }

    public Publication(int id, User user, String text, List<Image> images, List<Video> videos, List<Audio> audios, Coordinates coordinates, List<Comment> comments) {
        this.id = id;
        this.user = user;
        this.text = text;
       this.images = images;
        this.videos = videos;
        this.audios = audios;
        this.coordinates = coordinates;
        this.comments = comments;
    }

    public Publication(User user, String text, List<Image> images, List<Video> videos, List<Audio> audios, List<Comment> comments) {
        this.user = user;
        this.text = text;
       this.images = images;
        this.videos = videos;
        this.audios = audios;
        this.comments = comments;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public Set<User> getUserlikes() {
        return userlikes;
    }

    public void setUserlikes(Set<User> userlikes) {
        this.userlikes = userlikes;
    }

    public boolean addLike(User u) {
        boolean add = false;
        if (!userlikes.contains(u)) {
            add = userlikes.add(u);
            u.getLikes().add(this);
        }
        return add;
    }

    public boolean removeLike(User u) {
        boolean add = false;
        if (userlikes.contains(u)) {
            add = userlikes.remove(u);
            u.getLikes().remove(this);
        }
        return add;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }

    public List<Audio> getAudios() {
        return audios;
    }

    public void setAudios(List<Audio> audios) {
        this.audios = audios;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
        if(this.coordinates.getPublication() == null || (this.coordinates.getPublication() != null && this.coordinates.getPublication() != this)){
            this.coordinates.setPublication(this);
        }
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public boolean addComment(Comment c) {
        boolean add = false;
        if (!comments.contains(c)) {
            add = comments.add(c);
        }
        return add;
    }
   public boolean addAudio(Audio a){
        boolean add = false;
        if(!audios.contains(a)){
            add = audios.add(a);
        }
        return add;
    }
    public boolean addImage(Image i){
        boolean add = false;
        if(!images.contains(i)){
            add = images.add(i);
        }
        return add;
    }
    public boolean addVideo(Video v){
        boolean add = false;
        if(!videos.contains(v)){
            add = videos.add(v);
        }
        return add;
    }

    public boolean removeComment(Comment c) {
        return comments.remove(c);
    }
    public boolean removeVideo(Video v){
        return videos.remove(v);
    }
    public boolean removeAudio(Audio a){
        return audios.remove(a);
    }
    public boolean removeImage(Image i){
        return images.remove(i);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Publication that = (Publication) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
