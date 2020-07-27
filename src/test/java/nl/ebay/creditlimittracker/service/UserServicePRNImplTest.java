package nl.ebay.creditlimittracker.service;

import info.solidsoft.mockito.java8.api.WithBDDMockito;
import lombok.extern.slf4j.Slf4j;
import nl.ebay.creditlimittracker.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class UserServicePRNImplTest implements WithBDDMockito {
    UserServicePRNImpl underTest;

    @BeforeEach
    void setup() {
        underTest = new UserServicePRNImpl("16,38,47,61,74,82");
    }

    @Test
    @DisplayName("SHOULD return user from CSV file")
    void getUsers_list() {
        List<User> users = underTest.getUserData();
        assertEquals(users.size(), 7);
    }
}
