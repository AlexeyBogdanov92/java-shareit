package ru.practicum.shareit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.practicum.shareit.exception.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ErrorHandlerTest {
    ErrorHandler errorHandler;

    @BeforeEach
    void setUp() {
        errorHandler = new ErrorHandler();
    }

    @Test
    void handleNotFoundException() {
        NotFoundException notFoundException = new NotFoundException("Smth is not found");

        ErrorResponse response = errorHandler.handleNotFoundException(notFoundException);

        assertEquals(notFoundException.getMessage(), response.getError());
    }

    @Test
    void handleConflictException() {
        ConflictException conflictException = new ConflictException("Smth is wrong");

        ErrorResponse response = errorHandler.handleConflictException(conflictException);

        assertEquals(conflictException.getMessage(), response.getError());
    }

    @Test
    void handleUnsupportedOperationException() {
        UnsupportedOperationException unsupportedOperationException = new UnsupportedOperationException("Smth is not support");

        ErrorResponse response = errorHandler.handleUnsupportedOperationException(unsupportedOperationException);

        assertEquals(unsupportedOperationException.getMessage(), response.getError());
    }

    @Test
    void handleBookingException() {
        BookingStatusException statusException = new BookingStatusException("Smth With Booking");

        ErrorResponse response = errorHandler.handleBookingException(statusException);

        assertEquals(statusException.getMessage(), response.getError());
    }
}
