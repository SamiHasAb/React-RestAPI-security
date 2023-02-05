package org.study.pma.services;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;
import org.study.pma.models.User;
import org.study.pma.repositories.UserRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CustomUserDetailsServiceTest {


    @Autowired
    private CustomUserDetailsService service;
    @MockBean
    private UserRepository repository;

    User mockedUser = new User();
    @BeforeAll
    void setUp() {
        mockedUser.setUsername("user name");
        mockedUser.setId(1L);
    }

    @Test
    void loadUserByUsername_should_return_a_user() {

        when(repository.findByUsername(any()))
                .thenReturn(mockedUser);

        UserDetails actual = service.loadUserByUsername("user name");

        assertEquals(mockedUser.getUsername(),actual.getUsername());
    }

    @Test
    void loadUserByUsername_should_return_an_exception() {
       when(repository.findByUsername("user name")).thenReturn(null);

        UsernameNotFoundException actual = assertThrows(UsernameNotFoundException.class,
                () -> service.loadUserByUsername("user name"));

        assertEquals("User not found",actual.getMessage());
    }

    @Test
    void loadUserById_should_return_a_user() {

        when(repository.getById(1L)).thenReturn(mockedUser);

        User actual = service.loadUserById(1L);

        assertEquals(mockedUser.getId(), actual.getId());
    }

    @Test
    void loadUserById_should_return_an_exception(){

        when(repository.getById(1L)).thenReturn(null);

        UsernameNotFoundException actual = assertThrows(UsernameNotFoundException.class,
                () -> service.loadUserById(1L));

        assertEquals("User not found", actual.getMessage());
    }
}