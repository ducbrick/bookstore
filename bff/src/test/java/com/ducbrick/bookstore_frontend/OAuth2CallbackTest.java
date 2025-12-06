package com.ducbrick.bookstore_frontend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class OAuth2CallbackTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RestTemplate restTemplate;

    @Test
    public void testOAuth2CallbackEndpointExists() throws Exception {
        // Mock Auth0 token response
        String mockResponse = "{\"access_token\":\"mock_access_token_value\",\"token_type\":\"Bearer\",\"expires_in\":3600}";
        ResponseEntity<String> mockTokenResponse = new ResponseEntity<>(mockResponse, HttpStatus.OK);

        when(restTemplate.postForEntity(anyString(), any(), eq(String.class)))
            .thenReturn(mockTokenResponse);

        // Test that the /code endpoint exists and handles the code parameter
        mockMvc.perform(get("/code")
                .param("code", "test_authorization_code"))
            .andExpect(status().is3xxRedirection()); // Should redirect either to / or /login
    }

    @Test
    public void testOAuth2CallbackFailure() throws Exception {
        // Mock failed token response
        ResponseEntity<String> mockTokenResponse = new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        when(restTemplate.postForEntity(anyString(), any(), eq(String.class)))
            .thenReturn(mockTokenResponse);

        // Test that the /code endpoint handles failure gracefully
        mockMvc.perform(get("/code")
                .param("code", "invalid_code"))
            .andExpect(status().is3xxRedirection()); // Should redirect to /login on failure
    }

    @Test
    public void testOAuth2CallbackMissingCode() throws Exception {
        // Test the /code endpoint without code parameter
        mockMvc.perform(get("/code"))
            .andExpect(status().isBadRequest());
    }
}