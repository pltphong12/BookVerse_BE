package com.example.bookverse.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReqBookImageDTO {

    @NotBlank(message = "relativePath isn't blank")
    private String relativePath;

    private int sortOrder;

    private boolean primary;
}
