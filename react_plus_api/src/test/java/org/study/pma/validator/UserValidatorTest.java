package org.study.pma.validator;

import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.study.pma.models.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("Test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserValidatorTest {

    UserValidator mockedUserValidator;
    Errors errors;
    @BeforeAll
    void setUp() {
        mockedUserValidator = spy(UserValidator.class);

    }

    @BeforeEach void beforeEach(){
        errors = mock(Errors.class);
    }


    @Test
    void supports() {

        assertTrue(mockedUserValidator.supports(User.class));
        assertFalse(mockedUserValidator.supports(org.springframework.security.core.userdetails.User.class));
    }

    @Test
    void validate(){

        User validPassword = new User();
        validPassword.setPassword("123456");
        validPassword.setConfirmPassword("123456");

        mockedUserValidator.validate(validPassword,errors);

        verifyNoInteractions(errors);
    }

    @Test
    void validate_unmatchedPasswords(){

        User unmatchedPassword = new User();
        unmatchedPassword.setPassword("123456");
        unmatchedPassword.setConfirmPassword("123");

        mockedUserValidator.validate(unmatchedPassword,errors);

        verify(errors, times(1)).
                rejectValue("confirmPassword","Match", "Passwords must match");
    }

    @Test
    void validate_shortPassword() {

        User shortPassword = new User();
        shortPassword.setPassword("1234");

        mockedUserValidator.validate( shortPassword, errors );

        verify(errors, times(1))
                .rejectValue("password","Length", "Password must be at least 6 characters");
    }
}