package kz.kartayev.authorization_service.commons.adapter;

import kz.kartayev.authorization_service.commons.dto.CustomerDto;
import kz.kartayev.authorization_service.commons.dto.OrderDetailsDto;
import kz.kartayev.authorization_service.commons.dto.OrderDto;
import kz.kartayev.authorization_service.commons.dto.PriceDto;
import kz.kartayev.authorization_service.entity.OrderDetails;
import lombok.Builder;
import org.aspectj.weaver.ast.Or;

@Builder
public class OrderDetailsDtoAdapter {
  public static OrderDetailsDto toDto(OrderDetails orderDetails) {
    return OrderDetailsDto.builder()
            .id(orderDetails.getId())
            .price(orderDetails.getPrice())
            .orderDto(OrderDto.builder()
                    .id(orderDetails.getOrder().getId())
                    .buyerDto(UserDtoAdapter.toDto(orderDetails.getOrder().getBuyer()))
                    .createdDate(orderDetails.getOrder().getCreatedDate())
                    .deliveryDate(orderDetails.getOrder().getDeliveryDate())
                    .isPayed(orderDetails.getOrder().getIsPayed())
                    .deliveryAddress(orderDetails.getOrder().getDeliveryAddress())
                    .build())
            .customerDto(CustomerDto.builder()
                    .customerName(orderDetails.getUserCustomer().getName())
                    .build())
            .deliveryStatus(orderDetails.getDeliveryStatus())
            .productDto(ProductAdapter.toDto(orderDetails.getProduct()))
            .deliveryCode(orderDetails.getDeliveryCode())
            .quantity(orderDetails.getQuantity())
            .build();
  }
}
