package com.markets.demo.business.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PagingDTO<T> {

    private Iterable<T> content;

    private int pageNumber;

    private int totalPages;

    private long totalElements;

}
