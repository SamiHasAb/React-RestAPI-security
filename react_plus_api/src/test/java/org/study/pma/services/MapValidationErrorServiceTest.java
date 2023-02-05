package org.study.pma.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

/*
 * to test without Autowired
 *  @ExtendWith(MockitoExtension.class)
 */
@SpringBootTest()
@ActiveProfiles("test")
class MapValidationErrorServiceTest {

    @Autowired
    private MapValidationErrorService mapValidationErrorService;
    @Mock
    private BindingResult result;

    private FieldError error;

    @BeforeEach()
    void setUp() {
        error = new FieldError("object name",
                "field name", "default message");
    }

    @Test
    void should_return_error() {

        ResponseEntity<Map<String, String>> expected = new ResponseEntity<>(
                Map.of("field name", "default message"), HttpStatus.BAD_REQUEST);

        when(result.hasErrors()).thenReturn(true);
        when(result.getFieldErrors()).thenReturn(List.of(error));

        ResponseEntity<?> actual = mapValidationErrorService.mapValidationService(result);

        assertEquals(expected, actual);
    }

    @Test
    void should_return_null() {

        when(result.hasErrors()).thenReturn(false);

        ResponseEntity<?> actual = mapValidationErrorService.mapValidationService(result);

        assertNull(actual);
    }
}