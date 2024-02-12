package com.example.datastorage;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    // Shared preferences instance and editor
    private final SharedPreferences sp;
    private final SharedPreferences.Editor editor;

    // Keys for session management
    private final String KEY_IF_LOGGED_IN = "key_session_if_logged_in";
    private final String KEY_NAME = "key_session_name";
    private final String KEY_CONTACT = "key_session_contact";

    // Constructor
    public SessionManager(Context context) {
        // Context for shared preferences
        String PREF_FILE_NAME = "library";
        int PRIVATE_MODE = 0;
        sp = context.getSharedPreferences(PREF_FILE_NAME, PRIVATE_MODE);
        editor = sp.edit();
    }

    // Check if the user is logged in
    public boolean checkSession() {
        return sp.contains(KEY_IF_LOGGED_IN);
    }

    // Create a new session with user details
    public void createSession(String username, String email, String contact) {
        editor.putString(KEY_NAME, username);
        String KEY_EMAIL = "key_session_email";
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_CONTACT, contact);
        editor.putBoolean(KEY_IF_LOGGED_IN, true);
        editor.apply();
    }

    // Get session details based on the provided key
    public String getSessionDetails(String key) {
        return sp.getString(key, null);
    }

    // Logout the current session
    public void logoutSession() {
        editor.clear();
        editor.apply();
    }

    // Save and apply user's name to the session
    public String saveName(String name) {
        editor.putString(KEY_NAME, name);
        editor.apply();
        return name;
    }

    // Save and apply user's contact to the session
    public String saveContact(String contact) {
        editor.putString(KEY_CONTACT, contact);
        editor.apply();
        return contact;
    }
}
