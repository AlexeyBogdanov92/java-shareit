package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDtoRequest;
import ru.practicum.shareit.request.dto.ItemRequestDtoResponse;


import java.util.List;

public interface ItemRequestService {
    ItemRequestDtoResponse createRequest(long userId, ItemRequestDtoRequest requestDto);

    List<ItemRequestDtoResponse> getRequestsByOwner(long userId, Integer from, Integer size);

    List<ItemRequestDtoResponse> getAllRequests(long userId, Integer from, Integer size);

    ItemRequestDtoResponse getRequestsById(long userId, long requestId);
}
