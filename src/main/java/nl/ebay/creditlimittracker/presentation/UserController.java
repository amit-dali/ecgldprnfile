package nl.ebay.creditlimittracker.presentation;

import lombok.extern.slf4j.Slf4j;
import nl.ebay.creditlimittracker.model.User;
import nl.ebay.creditlimittracker.service.SortByName;
import nl.ebay.creditlimittracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
@Validated
public class UserController {

    private final UserService userServiceCSV;
    private final UserService userServicePRN;

    @Autowired
    public UserController(@Qualifier(value = "userServiceCSVImpl") UserService userServiceCSV,
                          @Qualifier(value = "userServicePRNImpl") UserService userServicePRN) {
        this.userServiceCSV = userServiceCSV;
        this.userServicePRN = userServicePRN;
    }

    @GetMapping(path = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public CollectionModel<User> getUsers() {
        List<User> usersInCSV = userServiceCSV.getUserData();
        List<User> usersInPrn = userServicePRN.getUserData();
        List<User> users = appendAndSortUsers(usersInCSV, usersInPrn);
        users.forEach(user -> {
            user.add(linkTo(methodOn(UserController.class)
                    .getUser(user.getName())).withRel("users"));
        });
        return CollectionModel.of(users, linkTo(methodOn(UserController.class).getUsers()).withSelfRel());
    }

    @GetMapping(path = "/users/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CollectionModel<User> getUser(@PathVariable @Valid @NotEmpty @NotBlank String name) {

        List<User> usersInCSV = userServiceCSV.getUserDataByName(name);
        List<User> usersInPrn = userServicePRN.getUserDataByName(name);
        List<User> users = appendAndSortUsers(usersInCSV, usersInPrn);
        users.forEach(user -> {
            user.add(linkTo(methodOn(UserController.class)
                    .getUser(user.getName())).withRel("users"));
        });
        return CollectionModel.of(users);
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
