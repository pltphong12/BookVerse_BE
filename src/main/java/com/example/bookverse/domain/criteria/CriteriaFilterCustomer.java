package com.example.bookverse.domain.criteria;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CriteriaFilterCustomer {
    private String identityCard;
    private String customerLevel;
    private LocalDate dateFrom;
}
