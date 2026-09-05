package com.example.bookverse.dto.response;

import com.example.bookverse.domain.CustomerAddress;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResCustomerAddressDTO {
    private long id;
    private String receiverName;
    private String receiverPhone;
    private String province;
    private String ward;
    private String addressLine;
    private String fullAddress;
    private boolean isDefault;

    public static ResCustomerAddressDTO from(CustomerAddress address) {
        ResCustomerAddressDTO result = new ResCustomerAddressDTO();
        result.setId(address.getId());
        result.setReceiverName(address.getReceiverName());
        result.setReceiverPhone(address.getReceiverPhone());
        result.setProvince(address.getProvince());
        result.setWard(address.getWard());
        result.setAddressLine(address.getAddressLine());
        result.setFullAddress(address.getFullAddress());
        result.setDefault(address.isDefault());
        return result;
    }
}
