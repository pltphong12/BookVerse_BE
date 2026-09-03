package com.example.bookverse.service;

import com.example.bookverse.dto.request.ReqCustomerAddressDTO;
import com.example.bookverse.dto.response.ResCustomerAddressDTO;

import java.util.List;

public interface CustomerAddressService {
    ResCustomerAddressDTO create(ReqCustomerAddressDTO request) throws Exception;

    ResCustomerAddressDTO update(ReqCustomerAddressDTO request) throws Exception;

    List<ResCustomerAddressDTO> listMine() throws Exception;

    void delete(long id) throws Exception;
}
