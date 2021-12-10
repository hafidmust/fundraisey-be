package com.fundraisey.backend.model;

import lombok.Data;

import java.util.List;

@Data
public class LoanResponseModel {
    List<LoanModel> content;
    Long totalElements;
    Integer totalPages;
    Integer numberOfElements;
    Integer pageNumber;
    Integer size;
}
