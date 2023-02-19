package org.study.pma.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.study.pma.models.User;
import org.study.pma.security.JwtAuthenticationEntryPoint;
import org.study.pma.security.JwtTokenProvider;
import org.study.pma.services.CustomUserDetailsService;
import org.study.pma.services.MapValidationErrorService;
import org.study.pma.services.UserService;
import org.study.pma.validator.UserValidator;

import java.beans.PropertyEditor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("Test")
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;
    @MockBean
    MapValidationErrorService mapValidationErrorService;

    @MockBean
    UserValidator userValidator;

    @MockBean
    AuthenticationManager authenticationManager;

    @MockBean
    JwtTokenProvider jwtTokenProvider;

    @MockBean
    JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @MockBean
    CustomUserDetailsService customUserDetailsService;

    @MockBean
    BCryptPasswordEncoder bCryptPasswordEncoder;

    User user;
    @BeforeEach
    void setUp() {

        user = new User();
        user.setId(10L);
        user.setUsername("username@mail.com");
        user.setFullName("full name");
        user.setPassword("123456");
        user.setConfirmPassword("123456");

    }

    @Test
    void registerUser() throws Exception {

        String registerUser = "{\n" +
                "  \"username\": \"username@mail.com\",\n" +
                "  \"fullName\": \"full name\",\n" +
                "  \"password\": \"123456\",\n" +
                "  \"confirmPassword\": \"123456\",\n" +
                "  \"projects\": [\n" +
                "  ]\n" +
                "}";

        when(mapValidationErrorService.mapValidationService(any(BindingResult.class)))
                .thenReturn(null);
        when(userService.saveUser(any(User.class)))
                .thenReturn(user);

        mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(registerUser)).andExpect(status().isCreated());
    }

//    @Test @Disabled
//    void registerUser_returnsErrors() throws Exception {
//
//        String registerUser = "{\n" +
//                "  \"username\": \"usernamemail\",\n" +
//                "  \"password\": \"123456\",\n" +
//                "  \"confirmPassword\": \"1234567\",\n" +
//                "  \"projects\": [\n" +
//                "  ]\n" +
//                "}";
//
//
//        Map<String, String> errorMap = new HashMap<>();
//        errorMap.put("fullName", "Please enter your full name");
//        ResponseEntity<Map<String, String>> errorResponseEntity =
//                new ResponseEntity<Map<String, String>>(errorMap, HttpStatus.BAD_REQUEST);
//
//        when(mapValidationErrorService.mapValidationService(any(BindingResult.class)))
//                .thenReturn(errorResponseEntity);
////        when(userService.saveUser(any(User.class)))
////                .thenReturn(user);
//
//        mockMvc.perform(post("/api/users/register")
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .content(registerUser)).andExpect(status().isCreated());
//    }


    @Test
    void authenticateUser() {
    }


}