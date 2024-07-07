package ru.practicum.shareit.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ErrorHandlerTest {
    private ErrorHandler errorHandler;

    @BeforeEach
    public void setUp() {
        errorHandler = new ErrorHandler();
    }

    @Test
    public void handleNotFoundException() {
        NotFoundException notFoundException = new NotFoundException("Smth is not found");

        ErrorResponse response = errorHandler.handleNotFoundException(notFoundException);

        assertEquals(notFoundException.getMessage(), response.getError());
    }

    @Test
    public void handleConflictException() {
        ConflictException conflictException = new ConflictException("Smth is wrong");

        ErrorResponse response = errorHandler.handleConflictException(conflictException);

        assertEquals(conflictException.getMessage(), response.getError());
    }

    @Test
    public void handleUnsupportedOperationException() {
        UnsupportedOperationException unsupportedOperationException = new UnsupportedOperationException("Smth is not support");

        ErrorResponse response = errorHandler.handleUnsupportedOperationException(unsupportedOperationException);

        assertEquals(unsupportedOperationException.getMessage(), response.getError());
    }

    @Test
    public void handleBookingException() {
        BookingStatusException statusException = new BookingStatusException("Smth With Booking");

        ErrorResponse response = errorHandler.handleBookingException(statusException);

        assertEquals(statusException.getMessage(), response.getError());
    }
}