package com.example.bookverse.service;

import com.example.bookverse.dto.criteria.CriteriaFilterOrder;
import com.example.bookverse.dto.request.ReqCreateOrderDTO;
import com.example.bookverse.dto.request.ReqUpdateOrderDTO;
import com.example.bookverse.dto.response.ResOrderDTO;
import com.example.bookverse.dto.response.ResPagination;

import org.springframework.data.domain.Pageable;

public interface OrderService {

    ResOrderDTO create(ReqCreateOrderDTO req) throws Exception;

    ResOrderDTO update(ReqUpdateOrderDTO req) throws Exception;

    ResOrderDTO getById(long id) throws Exception;

    ResPagination listMine(Pageable pageable) throws Exception;

    ResPagination fetchAllOrdersWithPaginationAndFilter(CriteriaFilterOrder criteria, Pageable pageable) throws Exception;

    void cancel(long id) throws Exception;
}
