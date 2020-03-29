package com.photos.services;

import com.photos.model.Picture;
import com.photos.model.User;
import com.photos.repositories.PictureRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class PictureServiceImpl implements PictureService {

    private PictureRepository pictureRepository;

    private UserService userService;

    public PictureServiceImpl(PictureRepository pictureRepository, UserService userService){
        this.pictureRepository = pictureRepository;
        this.userService = userService;
    }

@Override
    public Picture savePicture(Picture picture) {
    return this.pictureRepository.saveAndFlush(picture);
}

    public List<Picture> findAll() {
        return pictureRepository.findAll();
    }

    @Override
    public List<Picture> findByUser(String userEmail) {
        if(userEmail == null){
           return  Collections.EMPTY_LIST;
        }
        User userByEmail = userService.findUserByEmail(userEmail);
        return new ArrayList<>(userByEmail.getPictures());
    }


}
