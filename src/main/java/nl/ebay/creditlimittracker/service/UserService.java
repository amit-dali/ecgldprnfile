package nl.ebay.creditlimittracker.service;

import nl.ebay.creditlimittracker.model.User;

import java.util.List;

public interface UserService {
    /**
     * Services fetches user information from a file
     *
     * @return list of users
     */
    List<User> getUserData();
}
