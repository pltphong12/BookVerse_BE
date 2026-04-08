package com.example.bookverse.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReqOrderLineDTO {

    @NotNull(message = "bookId không được để trống")
    private Long bookId;

    @NotNull(message = "quantity không được để trống")
    @Min(value = 1, message = "quantity phải >= 1")
    private Long quantity;
}
