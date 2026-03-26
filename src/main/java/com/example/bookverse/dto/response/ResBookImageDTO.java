package com.example.bookverse.dto.response;

import com.example.bookverse.domain.BookImage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResBookImageDTO {
    private long id;
    private String relativePath;
    private int sortOrder;
    private boolean primary;

    public static ResBookImageDTO from(BookImage img) {
        return new ResBookImageDTO(
                img.getId(),
                img.getRelativePath(),
                img.getSortOrder(),
                img.isPrimaryImage());
    }
}
