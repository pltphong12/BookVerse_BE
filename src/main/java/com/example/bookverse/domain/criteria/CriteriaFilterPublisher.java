package com.example.bookverse.domain.criteria;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CriteriaFilterPublisher {
    private String name;
    private LocalDate dateFrom;
}
