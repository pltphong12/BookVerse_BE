package com.example.bookverse.service.impl;

import com.example.bookverse.domain.Customer;
import com.example.bookverse.domain.CustomerAddress;
import com.example.bookverse.dto.request.ReqCustomerAddressDTO;
import com.example.bookverse.dto.response.ResCustomerAddressDTO;
import com.example.bookverse.exception.global.IdInvalidException;
import com.example.bookverse.repository.CustomerAddressRepository;
import com.example.bookverse.service.CustomerAddressService;
import com.example.bookverse.util.CurrentCustomerAccessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CustomerAddressServiceImpl implements CustomerAddressService {
    private final CustomerAddressRepository customerAddressRepository;
    private final CurrentCustomerAccessor currentCustomerAccessor;

    public CustomerAddressServiceImpl(CustomerAddressRepository customerAddressRepository,
                                      CurrentCustomerAccessor currentCustomerAccessor) {
        this.customerAddressRepository = customerAddressRepository;
        this.currentCustomerAccessor = currentCustomerAccessor;
    }

    @Override
    @Transactional
    public ResCustomerAddressDTO create(ReqCustomerAddressDTO request) throws Exception {
        Customer customer = currentCustomerAccessor.requireCurrentCustomer();
        CustomerAddress address = new CustomerAddress();
        copyRequest(request, address);
        address.setCustomer(customer);
        if (Boolean.TRUE.equals(request.getIsDefault())
                || customerAddressRepository.findAllByCustomerOrderByIsDefaultDescCreatedAtDesc(customer).isEmpty()) {
            clearDefaultAddress(customer);
            address.setDefault(true);
        }
        return ResCustomerAddressDTO.from(customerAddressRepository.save(address));
    }

    @Override
    @Transactional
    public ResCustomerAddressDTO update(ReqCustomerAddressDTO request) throws Exception {
        if (request.getId() == null) {
            throw new IdInvalidException("id địa chỉ không được để trống");
        }
        Customer customer = currentCustomerAccessor.requireCurrentCustomer();
        CustomerAddress address = findOwnedAddress(request.getId(), customer);
        copyRequest(request, address);
        if (Boolean.TRUE.equals(request.getIsDefault())) {
            clearDefaultAddress(customer);
            address.setDefault(true);
        }
        return ResCustomerAddressDTO.from(customerAddressRepository.save(address));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResCustomerAddressDTO> listMine() throws Exception {
        Customer customer = currentCustomerAccessor.requireCurrentCustomer();
        return customerAddressRepository.findAllByCustomerOrderByIsDefaultDescCreatedAtDesc(customer)
                .stream()
                .map(ResCustomerAddressDTO::from)
                .toList();
    }

    @Override
    @Transactional
    public void delete(long id) throws Exception {
        Customer customer = currentCustomerAccessor.requireCurrentCustomer();
        CustomerAddress address = findOwnedAddress(id, customer);
        if (address.isDefault()) {
            throw new IdInvalidException("Không thể xóa địa chỉ mặc định. Hãy đặt địa chỉ khác làm mặc định trước");
        }
        customerAddressRepository.delete(address);
    }

    private CustomerAddress findOwnedAddress(long id, Customer customer) throws IdInvalidException {
        return customerAddressRepository.findByIdAndCustomer(id, customer)
                .orElseThrow(() -> new IdInvalidException("Không tìm thấy địa chỉ giao hàng"));
    }

    private void clearDefaultAddress(Customer customer) {
        for (CustomerAddress address : customerAddressRepository
                .findAllByCustomerOrderByIsDefaultDescCreatedAtDesc(customer)) {
            if (address.isDefault()) {
                address.setDefault(false);
            }
        }
    }

    private void copyRequest(ReqCustomerAddressDTO source, CustomerAddress target) {
        target.setReceiverName(source.getReceiverName().trim());
        target.setReceiverPhone(source.getReceiverPhone().trim());
        target.setProvince(source.getProvince().trim());
        target.setWard(source.getWard().trim());
        target.setAddressLine(source.getAddressLine().trim());
    }
}
