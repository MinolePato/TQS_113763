
package com.example.demo.connection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

public class OpenWeatherServiceMockTest {

    private RestTemplate restTemplate;
    private MockRestServiceServer mockServer;

    @BeforeEach
    void setup() {
        restTemplate = new RestTemplate();
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void testWeatherApiMock() {
        String mockJson = """
        {
          "weather": [{"main": "Clouds", "description": "overcast clouds"}],
          "main": {"temp": 18.5}
        }
        """;

        String city = "Aveiro";
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=FAKE_KEY&units=metric";

        mockServer.expect(requestTo(url))
                .andRespond(withSuccess(mockJson, MediaType.APPLICATION_JSON));

        String response = restTemplate.getForObject(url, String.class);

        assertNotNull(response);
        assertTrue(response.contains("Clouds"));
        assertTrue(response.contains("18.5"));
    }
}
