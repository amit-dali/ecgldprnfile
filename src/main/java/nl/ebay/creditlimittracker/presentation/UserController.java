package nl.ebay.creditlimittracker.presentation;

import lombok.extern.slf4j.Slf4j;
import nl.ebay.creditlimittracker.model.User;
import nl.ebay.creditlimittracker.service.SortByName;
import nl.ebay.creditlimittracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
public class UserController {

    private final UserService userServiceCSV;
    private final UserService userServicePrn;

    @Autowired
    public UserController(@Qualifier(value = "userServiceCSVImpl") UserService userServiceCSV,
                          @Qualifier(value = "userServicePRNImpl") UserService userServicePrn) {
        this.userServiceCSV = userServiceCSV;
        this.userServicePrn = userServicePrn;
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        List<User> users = userServicePrn.getUserData();
        users.addAll(userServiceCSV.getUserData());
        Collections.sort(users, new SortByName());
        return users;
    }
}
