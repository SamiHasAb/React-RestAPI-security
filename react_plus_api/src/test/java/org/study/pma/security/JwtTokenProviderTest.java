package org.study.pma.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.DefaultJwtParser;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;
import org.study.pma.models.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("Test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JwtTokenProviderTest {

    JwtTokenProvider jwtTokenProvider;

    Authentication mockedAuthentication;

    Jwts mockedJwts;

    DefaultJwtParser defaultJwtParser;


    @BeforeAll
    void setUp() {
        jwtTokenProvider = spy(JwtTokenProvider.class);
    }

    @BeforeEach
    void beforeEach() {
        mockedAuthentication = mock(Authentication.class);
        mockedJwts = spy(Jwts.class);
        defaultJwtParser = mock(DefaultJwtParser.class);
    }


    @Test
    void generateToken() {

        User user = new User();
        user.setId(1L);
        user.setUsername("username");
        user.setFullName("full name");

        when(mockedAuthentication.getPrincipal()).thenReturn(user);

        String actual = jwtTokenProvider.generateToken(mockedAuthentication);

        assertFalse(actual.isBlank());
    }

    @Test @Disabled("need a token that is not expired to pass")
    void getUserIdFromJWT() {

        String token = "eyJhbGciOiJIUzUxMiJ9.eyJmdWxsTmFtZSI6ImZ1bGwgbmFtZSIsImlkIjoiMSIsImV4cCI6MTY3NjEzMD" +
                "ExNSwiaWF0IjoxNjc2MTI5ODE1LCJ1c2VybmFtZSI6InVzZXJuYW1lIn0.Uipixj08qpjCL-bXIUIxXRsFKHs97pTr" +
                "MtOETJ4mZvrXFDuqHvPpOiVrGpA4-Xqtclg75TqzKPh2_xxljK7EqQ";

        Long actual = jwtTokenProvider.getUserIdFromJWT(token);

        assertEquals(1, actual);
    }

    @Test @Disabled("need to figure out how to test JWT token validation")
    void validateToken() {

    }
}