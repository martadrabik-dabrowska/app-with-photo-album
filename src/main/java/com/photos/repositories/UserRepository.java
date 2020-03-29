package com.photos.repositories;

import com.photos.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, Integer> {

    User findByEmail(String email);

    @Modifying
    @Query(value = "UPDATE User u SET u.firstName = :newFirstName, u.lastName = :newLastName WHERE u.email= :email")
    void updateUser(@Param("newFirstName")String firstName, @Param("newLastName") String lastName, @Param("email") String email);
}
