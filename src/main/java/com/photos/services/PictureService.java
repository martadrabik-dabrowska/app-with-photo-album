package com.photos.services;

import com.photos.model.Picture;
import java.util.List;

public interface PictureService{

    Picture savePicture(Picture picture);

    List<Picture> findAll();

    List<Picture> findByUser(String userEmail);
}
