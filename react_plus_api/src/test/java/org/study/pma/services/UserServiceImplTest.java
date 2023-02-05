package org.study.pma.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.study.pma.exceptions.UsernameAlreadyExistsException;
import org.study.pma.models.User;
import org.study.pma.repositories.UserRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserServiceImplTest {

    @Autowired
    private UserService userService;
    @MockBean
    private UserRepository userRep;

    @Test
    void saveUser() {
        User mockedUser = new User();
        mockedUser.setUsername("username");
        mockedUser.setPassword("password");

        when(userRep.save(mockedUser))
                .thenReturn(mockedUser);

        User actual = userService.saveUser(mockedUser);

        assertEquals(mockedUser, actual);
    }

    @Test
    void saveUser_should_return_UsernameAlreadyExistsException() {
        User mockedUser = new User();
        mockedUser.setUsername("username");
        mockedUser.setPassword("password");

        when(userRep.save(mockedUser))
                .thenThrow( UsernameAlreadyExistsException.class);

        UsernameAlreadyExistsException actual =assertThrows(UsernameAlreadyExistsException.class,
                ()-> userService.saveUser(mockedUser));

        assertEquals("Username 'username' already exists", actual.getMessage());
    }
}