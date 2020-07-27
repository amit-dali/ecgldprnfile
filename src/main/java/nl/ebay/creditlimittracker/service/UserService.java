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

    /**
     * Service fetches user information from a file based on name
     *
     * @param name name of the user
     * @return list of credit limits of an user
     */
    List<User> getUserDataByName(String name);
}
