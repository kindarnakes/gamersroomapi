package com.angelserrano.gamersroomapi.dao;

import com.angelserrano.gamersroomapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepo extends JpaRepository<User, Integer> {


    @Query(
            value = "INSERT INTO friends(id1, id2) VALUES (?1,?2) RETURNING friends.id1",
            nativeQuery = true)
    Integer addFriend(int id1, int id2);

    @Query(
            value = "DELETE FROM friends WHERE id1 = ?1 AND id2 = ?2 RETURNING friends.id1",
            nativeQuery = true)
    Integer removeFriend(int id1, int id2);

    @Query(
            value = "(SELECT true FROM friends WHERE id1 = ?1 AND id2 = ?2)",
            nativeQuery = true
    )
    Boolean isFriend(int id1, int id2);


    @Query(
            value = "SELECT true FROM friends WHERE ((SELECT true FROM friends WHERE id1 = ?1 AND id2 = ?2)) " +
                    "AND ((SELECT true FROM friends WHERE id1 = ?2 AND id2 = ?1))",
            nativeQuery = true
    )
    Boolean mutualFriends(int id1, int id2);

    @Query(
            value = "SELECT u.* FROM users u WHERE u.privacy = 0 OR (u.privacy = 1  AND" +
                    "                                                (u.id IN" +
                    "                                                 (SELECT id1 FROM friends WHERE id2 = ?1 )" +
                    "                                                    ) AND (u.id IN " +
                    "                                                           (SELECT id2 FROM friends WHERE id1 = ?1 )" +
                    "                                                        )" +
                    "    )" +
                    " GROUP BY u.id",
            nativeQuery = true
    )
    List<User> getUsersAllowAccess(int id);

    @Query(
            value = "SELECT u.* FROM users u LEFT JOIN friends f on u.id = f.id1 WHERE (((u.id = ?2) " +
                    "AND (((u.privacy = 0)) OR " +
                    "((u.privacy = 1) AND ((SELECT true FROM friends WHERE id1 = ?1 AND id2 = ?2))" +
                    " AND ((SELECT true FROM friends WHERE id1 = ?2 AND id2 = ?1)))))) " +
                    "GROUP BY u.id",
            nativeQuery = true
    )
    User getUserAllowAccess(int id1, int id2);


    @Query(
            value = "SELECT * FROM users WHERE id IN (SELECT id1 FROM friends WHERE id2 = ?1 AND " +
                    "id1 NOT IN (SELECT id2 FROM friends WHERE id1 = ?1))",
            nativeQuery = true
    )
    List<User> getAllPetitions(int id);

    @Query(
            value = "SELECT * FROM users WHERE id IN (SELECT id1 FROM friends WHERE id2 = ?1 AND " +
                    "id1 IN (SELECT id2 FROM friends WHERE id1 = ?1))",
            nativeQuery = true
    )
    List<User> getAllFriends(int id);


    @Query(
            value = "SELECT * FROM users WHERE email = ?1 AND pass = ?2",
            nativeQuery = true
    )
    User logIn(String email, String pass);

    @Query(
            value = "SELECT u.* FROM users u WHERE (LOWER(u.username) LIKE %?1%) AND (u.privacy != 2)",
            nativeQuery = true
    )
    List<User> getUsersAllowAccessByName(String name);

    @Query(
            value = "SELECT u.* FROM users u WHERE (u.email = ?1) AND (u.privacy != 2)",
            nativeQuery = true
    )
    User getUsersAllowAccessByEmail(String email);

}
