package org.study.pma.security;

import org.apache.commons.logging.Log;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.study.pma.models.User;
import org.study.pma.services.CustomUserDetailsService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("Test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JwtAuthenticationFilterTest {

   @Autowired
   @InjectMocks
    JwtAuthenticationFilter jwtAuthenticationFilter;
    @MockBean
    JwtTokenProvider tokenProvider;
    @MockBean
    CustomUserDetailsService customUserDetailsService;

    @Mock
    HttpServletRequest httpServletRequest;
    @Mock
    HttpServletResponse httpServletResponse;
    @Mock
    FilterChain filterChain;
    @Mock
    SecurityContextHolder securityContextHolder;

    @Test
    void doFilterInternal() throws ServletException, IOException {

        User mockedUser = new User();

        when(tokenProvider.validateToken(any()))
                .thenReturn(true,true,true);
        when(httpServletRequest.getHeader("Authorization"))
                .thenReturn("Bearer token");
        when(tokenProvider.getUserIdFromJWT("token"))
                .thenReturn(10L);
        when(customUserDetailsService.loadUserById(10L))
                .thenReturn(mockedUser);

        jwtAuthenticationFilter.doFilterInternal(httpServletRequest,httpServletResponse,filterChain);

        verify(filterChain, times(1))
                .doFilter(any(HttpServletRequest.class),any(HttpServletResponse.class));
    }
}