package com.example.bookverse.dto.criteria;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CriteriaFilterUser {
    private String email;
    private long roleId = 0;
    private LocalDate dateFrom;
}
