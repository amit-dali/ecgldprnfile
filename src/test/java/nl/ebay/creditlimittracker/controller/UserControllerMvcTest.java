package nl.ebay.creditlimittracker.controller;

import info.solidsoft.mockito.java8.api.WithBDDMockito;
import nl.ebay.creditlimittracker.exception.handlers.FileParsingException;
import nl.ebay.creditlimittracker.model.User;
import nl.ebay.creditlimittracker.presentation.UserController;
import nl.ebay.creditlimittracker.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
public class UserControllerMvcTest implements WithBDDMockito {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    @Qualifier("userServiceCSVImpl")
    private UserService userServiceCSV;

    @MockBean
    @Qualifier("userServicePRNImpl")
    private UserService userServicePRN;

    @Test
    @DisplayName("SHOULD return list of users json sorted by name")
    void getUsers_200() throws Exception {
        // given

        User user1 = User.builder().name("xyz").address("adddress").creditLimit(1222).dateOfBirth("20/10/1990").build();
        User user2 = User.builder().name("abc").address("Prn adddress").creditLimit(12.22).dateOfBirth("19891990").build();
        User user3 = User.builder().name("mno").address("Prn adddress").creditLimit(12.22).dateOfBirth("19891990").build();

        given(userServiceCSV.getUserData()).willReturn(Collections.singletonList(user1));
        given(userServicePRN.getUserData()).willReturn(Arrays.asList(user2, user3));
        // when
        MockHttpServletResponse response = mockMvc
                .perform(get("/users")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn()
                .getResponse();
        // then
        assertEquals(200, response.getStatus());
        List<User> users = Arrays.asList(user2, user3, user1);

        assertEquals("{\"_embedded\":{\"userList\":[{\"name\":\"abc\",\"address\":\"Prn adddress\",\"postCode\":null,\"phone\":null,\"creditLimit\":12.22,\"dateOfBirth\":\"19891990\",\"backendSystem\":null,\"_links\":{\"users\":{\"href\":\"http://localhost/users/abc\"}}},{\"name\":\"mno\",\"address\":\"Prn adddress\",\"postCode\":null,\"phone\":null,\"creditLimit\":12.22,\"dateOfBirth\":\"19891990\",\"backendSystem\":null,\"_links\":{\"users\":{\"href\":\"http://localhost/users/mno\"}}},{\"name\":\"xyz\",\"address\":\"adddress\",\"postCode\":null,\"phone\":null,\"creditLimit\":1222.0,\"dateOfBirth\":\"20/10/1990\",\"backendSystem\":null,\"_links\":{\"users\":{\"href\":\"http://localhost/users/xyz\"}}}]},\"_links\":{\"self\":{\"href\":\"http://localhost/users\"}}}", response.getContentAsString());
        verify(userServiceCSV).getUserData();
        verify(userServicePRN).getUserData();
        verifyNoMoreInteractions(userServiceCSV, userServicePRN);
    }

    @Test
    @DisplayName("SHOULD return internal server error  when exception occurred while reading data from files")
    void getUsers_500() throws Exception {
        // given
        given(userServiceCSV.getUserData()).willThrow(FileParsingException.class);

        // when
        MockHttpServletResponse response = mockMvc
                .perform(get("/users")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn()
                .getResponse();

        // then
        assertEquals(500, response.getStatus());
        verify(userServiceCSV).getUserData();
        verifyNoMoreInteractions(userServiceCSV, userServicePRN);
    }

    @Test
    @DisplayName("SHOULD return list of users json sorted by name")
    void getUser_200() throws Exception {
        // given
        User user1 = User.builder().name("xyz").address("adddress").creditLimit(1222).dateOfBirth("20/10/1990").build();
        given(userServiceCSV.getUserDataByName("xyz")).willReturn(Collections.singletonList(user1));
        given(userServicePRN.getUserDataByName("xyz")).willReturn(Collections.EMPTY_LIST);

        // when
        MockHttpServletResponse response = mockMvc
                .perform(get("/users/{name}", "xyz")
                        .accept(MediaType.APPLICATION_JSON_VALUE))

                .andReturn()
                .getResponse();
        // then
        assertEquals(200, response.getStatus());
        List<User> users = Arrays.asList(user1);

        assertEquals("{\"_embedded\":{\"userList\":[{\"name\":\"xyz\",\"address\":\"adddress\",\"postCode\":null,\"phone\":null,\"creditLimit\":1222.0,\"dateOfBirth\":\"20/10/1990\",\"backendSystem\":null,\"_links\":{\"users\":{\"href\":\"http://localhost/users/xyz\"}}}]}}", response.getContentAsString());
        verify(userServiceCSV).getUserDataByName("xyz");
        verify(userServicePRN).getUserDataByName("xyz");
        verifyNoMoreInteractions(userServiceCSV, userServicePRN);
    }

    @Test
    @DisplayName("SHOULD return bad request when user name is empty")
    void getUser_400() throws Exception {
        // given
        // when
        MockHttpServletResponse response = mockMvc
                .perform(get("/users/{name}", " ")
                        .accept(MediaType.APPLICATION_JSON_VALUE))

                .andReturn()
                .getResponse();
        // then
        assertEquals(400, response.getStatus());
        verifyNoMoreInteractions(userServiceCSV, userServicePRN);
    }

    @Test
    @DisplayName("SHOULD return internal server error  when exception occurred while reading data from files")
    void getUser_500() throws Exception {
        // given
        given(userServiceCSV.getUserDataByName("test")).willThrow(FileParsingException.class);

        // when
        MockHttpServletResponse response = mockMvc
                .perform(get("/users/{name}","test")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn()
                .getResponse();

        // then
        assertEquals(500, response.getStatus());
        verify(userServiceCSV).getUserDataByName("test");
        verifyNoMoreInteractions(userServiceCSV, userServicePRN);
    }

}
