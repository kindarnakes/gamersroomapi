package com.angelserrano.gamersroomapi.model;

import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "users", schema = "public")
@JsonIgnoreProperties(value = {"publications", "comments", "friends", "likes"}, allowSetters = false, allowGetters = true)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "email", unique = true)
    private String email;
    @Column(name = "pass")
    private String pass;
    @Column(name = "portrait")
    private String portrait;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinTable(
            name = "friends",
            joinColumns = {@JoinColumn(name = "id1")},
            inverseJoinColumns = {@JoinColumn(name = "id2")}
    )
    @JsonIgnoreProperties({"friends", "comments", "publications", "description", "pass", "email", "portrait", "privacy", "username", "likes"})
    private Set<User> friends;

    @ManyToMany(cascade = {CascadeType.REMOVE})
    @JoinTable(
            name = "likes",
            joinColumns = {@JoinColumn(name = "id_user")},
            inverseJoinColumns = {@JoinColumn(name = "id_publication")}
    )
    @JsonIgnoreProperties({"comments", "user","text", "images", "time", "videos", "audios", "coordinates", "userlikes"})
    private Set<Publication> likes;

    @OneToMany(cascade = {CascadeType.REMOVE, CascadeType.PERSIST}, mappedBy = "user")
    @JsonIgnoreProperties({"comments", "user", "text", "images", "time", "videos", "audios", "coordinates", "userlikes"})
    private List<Publication> publications;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy = "user")
    @JsonIgnoreProperties({"user", "publication", "text"})
    private List<Comment> comments;

    @Column(name = "description")
    private String description;

    @Column(name = "username")
    private String username;

    //0: public
    //1: only friends
    //2: private
    @Column(name="privacy")
    private short privacy;


    public User() {
        this.privacy = 0;
        this.comments = new ArrayList<>();
        this.publications = new ArrayList<>();
        this.friends = new HashSet<>();
        this.likes = new HashSet<>();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String accessPass() {
        return pass;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Set<User> getFriends() {
        return friends;
    }

    public void setFriends(Set<User> friends) {
        this.friends = friends;
    }

    public List<Publication> getPublications() {
        return publications;
    }

    public void setPublications(List<Publication> publications) {
        if (publications != null) {
            publications.forEach(publication -> {
                publication.setUser(this);
            });
        }
        this.publications = publications;
    }

    public short getPrivacy() {
        return privacy;
    }

    public void setPrivacy(short privacy) {
        this.privacy = privacy;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<Publication> getLikes() {
        return likes;
    }

    public void setLikes(Set<Publication> likes) {
        this.likes = likes;
    }

    public boolean addFriend(User u) {
        boolean add = false;
        if (!friends.contains(u)) {
            add = friends.add(u);
        }
        return add;
    }



    public boolean addPublication(Publication p) {
        boolean add = false;
        if (!publications.contains(p)) {
            add = publications.add(p);
        }
        return add;
    }

    public boolean addComment(Comment c) {
        boolean add = false;
        if (!comments.contains(c)) {
            add = comments.add(c);
        }
        return add;
    }

    public boolean addLike(Publication p) {
        boolean add = false;
        if (!likes.contains(p)) {
            add = likes.add(p);
            p.getUserlikes().add(this);
        }
        return add;
    }

    public boolean removeLike(Publication p) {
        boolean add = false;
        if (likes.contains(p)) {
            add = likes.remove(p);
            p.getUserlikes().remove(this);
        }
        return add;
    }

    public boolean removeFriend(User u) {
        return friends.remove(u);
    }

    public boolean removePublication(Publication p) {
        return publications.remove(p);
    }

    public boolean removeComment(Comment c) {
        return comments.remove(c);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return email.equals(user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", portrait='" + portrait + '\'' +
                ", friends=" + friends +
                ", publications=" + publications +
                ", comments=" + comments +
                ", description='" + description + '\'' +
                ", username='" + username + '\'' +
                ", privacy=" + privacy +
                '}';
    }

}
