package com.photos.repositories;

import com.photos.model.Picture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PictureRepository extends JpaRepository<Picture, Integer> {

    @Query(value="select pic from Picture where pic.picture_id=:id", nativeQuery = true)
    List<Picture> findById(@Param("id") int id);

    @Modifying
    @Query(value = "UPDATE Picture pic SET pic.description = :newDescription WHERE pic.id= :id")
    void updateDescription(@Param("newDescription") String description, @Param("id") int id);







}
