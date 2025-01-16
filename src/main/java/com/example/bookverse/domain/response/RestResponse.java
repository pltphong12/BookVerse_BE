package com.example.bookverse.domain.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestResponse<T> {
    private int status;
    private String message;
    private String error;
    private T data;
}
