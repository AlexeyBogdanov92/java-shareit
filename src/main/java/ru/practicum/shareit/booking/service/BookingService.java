package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;

import java.util.List;

public interface BookingService {

    BookingDtoResponse createBooking(long userId, BookingDtoRequest bookingDto);

    BookingDtoResponse approvedBooking(long userId, boolean approved, long bookingId);

    BookingDtoResponse getBookingById(long userId, long bookingId);

    List<BookingDtoResponse> getSortBookingByUser(long userId, String stateStr);

    List<BookingDtoResponse> getSortBookingByOwner(long userId, String stateStr);

}