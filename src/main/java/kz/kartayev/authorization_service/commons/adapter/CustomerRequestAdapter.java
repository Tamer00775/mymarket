package kz.kartayev.authorization_service.commons.adapter;

import kz.kartayev.authorization_service.commons.dto.CustomerRequestDto;
import kz.kartayev.authorization_service.entity.CustomerRequest;

public class CustomerRequestAdapter {

  public static CustomerRequestDto toDto(CustomerRequest request) {
    return CustomerRequestDto.builder()
            .id(request.getRequestId())
            .customer(UserDtoAdapter.toDto(request.getCustomer()))
            .customerName(request.getCustomerName())
            .licenseNum(request.getLicenseNum())
            .status(request.getStatus())
            .build();
  }
}
