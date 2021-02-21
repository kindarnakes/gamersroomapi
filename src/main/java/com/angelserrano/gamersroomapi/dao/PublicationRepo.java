package com.angelserrano.gamersroomapi.dao;

import com.angelserrano.gamersroomapi.model.Publication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PublicationRepo extends JpaRepository<Publication, Integer> {


    @Query(
            value = "SELECT p.* FROM publication p LEFT JOIN users u on u.id = p.id_user LEFT JOIN friends f on u.id = f.id1" +
                    " WHERE  u.privacy!= 2 AND p.id_user = ?2 AND (u.privacy = 0 OR (u.privacy = 1  AND" +
                    "                    (u.id IN " +
                    "                       (SELECT id1 FROM friends WHERE id2 = ?1 )" +
                    "                    ) AND (u.id IN" +
                    "                       (SELECT id2 FROM friends WHERE id1 = ?1 )" +
                    "                       )" +
                    "                    ))" +
                    "GROUP BY p.id",
            nativeQuery = true
    )
    List<Publication> getPublicationsByIdUserAllowAccessByIdUser(Integer iduser, Integer idsearch);

    @Query(
            value = "SELECT p.* FROM publication p LEFT JOIN users u on u.id = p.id_user LEFT JOIN friends f on u.id = f.id1" +
                    " WHERE  u.id = ?1 OR (u.privacy!= 2 AND (u.privacy = 0 OR (u.privacy = 1  AND" +
                    "                    (u.id IN " +
                    "                       (SELECT id1 FROM friends WHERE id2 = ?1 )" +
                    "                    ) AND (u.id IN" +
                    "                       (SELECT id2 FROM friends WHERE id1 = ?1 )" +
                    "                       )" +
                    "                    )))" +
                    "GROUP BY p.id",
            nativeQuery = true
    )
    List<Publication> getPublicationsAllowAccessByIdUser(Integer iduser);

    @Query(
            value = "SELECT p.* FROM publication p LEFT JOIN users u on u.id = p.id_user, friends f" +
                    " WHERE  u.privacy != 2 AND p.id = ?2 AND (u.privacy = 0 OR (u.privacy = 1  AND" +
                    "                    (u.id IN " +
                    "                       (SELECT id1 FROM friends WHERE id2 = ?1 )" +
                    "                    ) AND (u.id IN" +
                    "                       (SELECT id2 FROM friends WHERE id1 = ?1 )" +
                    "                       )" +
                    "                    ))" +
                    "GROUP BY p.id",
            nativeQuery = true
    )
    Publication getPublicationsByIdAllowAccessByIdUser(Integer iduser, Integer idsearch);

    @Query(
            value = "INSERT INTO likes(id_user, id_publication) VALUES (?1,?2) RETURNING likes.id_publication",
            nativeQuery = true)
    Integer addLike(int iduser, int idpublication);

    @Query(
            value = "DELETE FROM likes WHERE id_user = ?1 AND id_publication = ?2 RETURNING likes.id_publication",
            nativeQuery = true)
    Integer removeLike(int iduser, int idpublication);


    @Query(
            value = "SELECT p.* FROM publication p LEFT JOIN users u on u.id = p.id_user LEFT JOIN friends f on u.id = f.id1" +
                    " WHERE  u.id = ?1 OR (u.privacy!= 2 AND (u.privacy = 0 OR (u.privacy = 1  AND" +
                    "                    (u.id IN " +
                    "                       (SELECT id1 FROM friends WHERE id2 = ?1 )" +
                    "                    ) AND (u.id IN" +
                    "                       (SELECT id2 FROM friends WHERE id1 = ?1 )" +
                    "                       )" +
                    "                    )))" +
                    "GROUP BY p.id LIMIT ?3 OFFSET ?2",
            nativeQuery = true
    )
    List<Publication> getPublicationsAllowAccessByIdUser(Integer iduser, int init, int nperpage);

    @Query(
            value = "SELECT p.* FROM publication p LEFT JOIN users u on u.id = p.id_user LEFT JOIN friends f on u.id = f.id1" +
                    " WHERE  u.privacy!= 2 AND p.id_user = ?2 AND (u.privacy = 0 OR (u.privacy = 1  AND" +
                    "                    (u.id IN " +
                    "                       (SELECT id1 FROM friends WHERE id2 = ?1 )" +
                    "                    ) AND (u.id IN" +
                    "                       (SELECT id2 FROM friends WHERE id1 = ?1 )" +
                    "                       )" +
                    "                    ))" +
                    "GROUP BY p.id LIMIT ?4 OFFSET ?3",
            nativeQuery = true
    )
    List<Publication> getPublicationsByIdUserAllowAccessByIdUser(Integer iduser, Integer idsearch, int init, int nperpage);
}
