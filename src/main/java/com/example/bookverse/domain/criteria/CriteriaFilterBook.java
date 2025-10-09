package com.example.bookverse.domain.criteria;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CriteriaFilterBook {
    private String title;
    private long publisherId = 0;
    private long authorId = 0;
    private long categoryId = 0;
    private LocalDate dateFrom;
}
