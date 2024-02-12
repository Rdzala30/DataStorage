package com.example.datastorage;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

// Entity annotation defines the table name in the database
@Entity(tableName = "users")
public class UserTable {

    // Primary key for the user table, auto-generates IDs
    @PrimaryKey(autoGenerate = true)
    public long userId;

    // Column for the username
    @ColumnInfo(name = "username")
    public String userName;

    // Column for the user's email
    @ColumnInfo(name = "user_email")
    public String userEmail;

    // Column for the user's password
    @ColumnInfo(name = "user_password")
    public String userPassword;

    // Column for the user's contact information
    @ColumnInfo(name = "user_contact")
    public String userContact;

    // Default constructor required by Room
    public UserTable() {
    }

    // Getter method to retrieve the user's full name
    public String getFullName() {
        return userName;
    }

    // Getter method to retrieve the user's contact information
    public String getContact() {
        return userContact;
    }
}
