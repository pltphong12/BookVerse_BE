package com.example.bookverse.dto.criteria;

import java.util.List;

import com.example.bookverse.dto.enums.CoverFormat;
import com.example.bookverse.dto.enums.SortType;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CriteriaFilterProduct {
    private String title;
    private List<Long> categoryId;
    private List<Long> publisherId;
    private List<Integer> publishYear;
    private List<CoverFormat> coverFormat;
    private Double minPrice;
    private Double maxPrice;
    private SortType sortType;
}
