package nl.ebay.creditlimittracker.controller;

import nl.ebay.creditlimittracker.CreditLimitTrackerApplication;
import nl.ebay.creditlimittracker.model.User;
import nl.ebay.creditlimittracker.presentation.UserController;
import nl.ebay.creditlimittracker.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
@ContextConfiguration(classes = {CreditLimitTrackerApplication.class})
public class UserControllerMvcTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    @Captor
    private ArgumentCaptor<User> storeArgumentCaptor;

    @Test
    @DisplayName("SHOULD return list of users json")
    void getUsers_200() throws Exception {
        // given

        User user = User.builder().name("Name").address("adddress").creditLimit(1222).dateOfBirth("20/10/1990").build();
        given(userService.getUserData()).willReturn(Collections.singletonList(user));
        // when
        MockHttpServletResponse response = mockMvc
                .perform(get("/users")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn()
                .getResponse();
        // then
        assertEquals(200, response.getStatus());
        assertEquals("{\"_embedded\":{\"storeList\":[{\"uuid\":\"uuid\",\"city\":\"city\",\"postalCode\":\"postalCode\",\"street\":\"street\",\"street2\":\"street2\",\"street3\":\"street3\",\"addressName\":\"addressName\",\"longitude\":5.0,\"latitude\":52.0,\"complexNumber\":\"complexNumber\",\"showWarningMessage\":true,\"todayOpen\":\"todayOpen\",\"locationType\":\"Supermarkt\",\"collectionPoint\":true,\"sapStoreID\":\"sapStoreID\",\"todayClose\":\"todayClose\",\"distance\":0.0,\"_links\":{\"stores\":{\"href\":\"http://localhost/stores/uuid\"}}}]},\"_links\":{\"self\":{\"href\":\"http://localhost/stores?longitude=52.3&latitude=5.2\"}}}", response.getContentAsString());
        verify(userService).getUserData();
        verifyNoMoreInteractions(userService);
    }
}
