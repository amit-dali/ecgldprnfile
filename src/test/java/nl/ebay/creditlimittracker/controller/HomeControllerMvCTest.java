package nl.ebay.creditlimittracker.controller;

import info.solidsoft.mockito.java8.api.WithBDDMockito;
import nl.ebay.creditlimittracker.CreditLimitTrackerApplication;
import nl.ebay.creditlimittracker.presentation.HomeController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ExtendWith(SpringExtension.class)
@WebMvcTest(HomeController.class)
@ContextConfiguration(classes = {CreditLimitTrackerApplication.class})
public class HomeControllerMvCTest implements WithBDDMockito {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("SHOULD return home page")
    void getHomePage_200() throws Exception {
        // given

        // when
        MockHttpServletResponse response = mockMvc
                .perform(get("/"))
                .andReturn()
                .getResponse();
        // then
        assertEquals(200, response.getStatus());
        assertNotNull(response.getContentAsString());
    }

    @Test
    @DisplayName("SHOULD return Not found")
    void getHomePage_invalid_url() throws Exception {
        // given

        // when
        MockHttpServletResponse response = mockMvc
                .perform(get("/home"))
                .andReturn()
                .getResponse();
        // then
        assertEquals(404, response.getStatus());
    }
}
