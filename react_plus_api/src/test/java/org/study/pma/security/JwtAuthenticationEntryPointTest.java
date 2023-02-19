package org.study.pma.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.AuthenticationException;
import org.springframework.test.context.ActiveProfiles;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("Test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JwtAuthenticationEntryPointTest {

    JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    HttpServletRequest httpServletRequest;

    HttpServletResponse httpServletResponse;

    AuthenticationException authenticationException;

    @BeforeEach
    void setUp() {
        jwtAuthenticationEntryPoint = spy(JwtAuthenticationEntryPoint.class);
        httpServletRequest = mock(HttpServletRequest.class);
        httpServletResponse = spy(HttpServletResponse.class);
        authenticationException = mock(AuthenticationException.class);
    }

    @Test @Disabled
    void commence() throws ServletException, IOException {



        jwtAuthenticationEntryPoint.commence(httpServletRequest, httpServletResponse,authenticationException);

        verify(httpServletResponse,times(1)).setContentType("application/json");
    }
}