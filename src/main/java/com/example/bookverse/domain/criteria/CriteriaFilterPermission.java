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
public class CriteriaFilterPermission {
    private String name;
    private String method;
    private String domain;
    private LocalDate dateFrom;
}
