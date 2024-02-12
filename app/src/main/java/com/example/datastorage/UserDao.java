package com.example.datastorage;

import androidx.room.Dao;
import androidx.room.Query;

@Dao
public interface UserDao {

    // Insert a new user into the users table
    @Query("INSERT INTO users(user_email, user_password, username, user_contact) VALUES (:userEmail, :userPassword, :userName, :userContact)")
    void insertUser(String userEmail, String userPassword, String userName, String userContact);

    // Retrieve a user by email and password for login
    @Query("SELECT * FROM users WHERE user_email = :userEmail AND user_password = :password")
    UserTable getUserByUserEmailAndPassword(String userEmail, String password);

    // Retrieve a user by email
    @Query("SELECT * FROM users WHERE user_email = :userEmail")
    UserTable getUserByUserEmail(String userEmail);

    // Check if a user with the given email already exists
    @Query("SELECT * FROM users WHERE user_email = :userEmail")
    UserTable ifUserEmailIsTaken(String userEmail);

    // Update the username for a given email
    @Query("UPDATE users SET username = :newName WHERE user_email = :userEmail")
    void updateUserName(String newName, String userEmail);


}
