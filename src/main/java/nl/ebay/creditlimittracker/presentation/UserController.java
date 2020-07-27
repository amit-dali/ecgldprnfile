package nl.ebay.creditlimittracker.presentation;

import lombok.extern.slf4j.Slf4j;
import nl.ebay.creditlimittracker.model.User;
import nl.ebay.creditlimittracker.service.SortByName;
import nl.ebay.creditlimittracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
public class UserController {

    private final UserService userServiceCSV;
    private final UserService userServicePRN;

    @Autowired
    public UserController(@Qualifier(value = "userServiceCSVImpl") UserService userServiceCSV,
                          @Qualifier(value = "userServicePRNImpl") UserService userServicePRN) {
        this.userServiceCSV = userServiceCSV;
        this.userServicePRN = userServicePRN;
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        List<User> usersInCSV = userServiceCSV.getUserData();
        List<User> usersInPrn = userServicePRN.getUserData();
        return appendAndSortUsers(usersInCSV, usersInPrn);
    }

    private List<User> appendAndSortUsers(List<User> usersInCSV, List<User> usersInPRN) {
        if (CollectionUtils.isEmpty(usersInCSV)) {
            return usersInPRN;
        }
        if (CollectionUtils.isEmpty(usersInPRN)) {
            return usersInCSV;
        }
        List<User> allUsersFromAllSources = new ArrayList<>();
        allUsersFromAllSources.addAll(usersInCSV);
        allUsersFromAllSources.addAll(usersInPRN);
        Collections.sort(allUsersFromAllSources, new SortByName());
        return allUsersFromAllSources;
    }
}
