package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.constant.Constant;
import ru.practicum.shareit.pagination.Paginator;

import static org.junit.jupiter.api.Assertions.*;

class PaginatorTest {

    @Test
    public void withSort_whenValidFromAndSize_thenReturnPageRequest() {
        int from = 0;
        int size = 5;
        PageRequest expectedPageRequest = PageRequest.of(from / size, size, Constant.SORT_BY_CREATED_DESC);

        PageRequest actualPageRequest = Paginator.withSort(from, size, Constant.SORT_BY_CREATED_DESC);

        assertEquals(expectedPageRequest, actualPageRequest);
    }

    @Test
    public void simplePage_whenValidFromAndSize_thenReturnPageRequest() {
        int from = 0;
        int size = 5;
        PageRequest expectedPageRequest = PageRequest.of(from / size, size);

        PageRequest actualPageRequest = Paginator.simplePage(from, size);

        assertEquals(expectedPageRequest, actualPageRequest);
    }
}