package com.photos.services;

import com.photos.model.Picture;
import com.photos.model.User;

import java.util.List;

public interface UserService {
    User findUserByEmail(String email);
    void saveUser(User user);
    List<User> findAll();
    void deleteUser(User user);
    void  updateUser(String firstName, String lastName, String email);
    void addPictureToUser(String userEmail, Picture savePicture);

}
