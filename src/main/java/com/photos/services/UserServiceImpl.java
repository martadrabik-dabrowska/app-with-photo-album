package com.photos.services;

import com.photos.model.Picture;
import com.photos.model.Role;
import com.photos.model.User;
import com.photos.repositories.RoleRepository;
import com.photos.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserServiceImpl(@Qualifier("userRepository") UserRepository userRepository,@Qualifier("roleRepository") RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void saveUser(User user) {
        user.setPassword(user.getPassword());
        user.setActive(1);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        Role role =roleRepository.findByRole("user");
        user.setRole(role);
        this.userRepository.save(user);
    }

    public void deleteUser(User user){
        userRepository.delete(user);
    }

    @Override
    public void updateUser(String firstName, String lastName, String email) {
        userRepository.updateUser(firstName, lastName, email);
    }

    @Override
    public void addPictureToUser(String userEmail, Picture savePicture) {
        User userByEmail = findUserByEmail(userEmail);
        Set<Picture> pictures = userByEmail.getPictures();
        pictures.add(savePicture);
        userByEmail.setPictures(pictures);
        userRepository.saveAndFlush(userByEmail);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

}
