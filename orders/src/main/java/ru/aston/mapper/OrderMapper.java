package ru.aston.mapper;

import ru.aston.dto.NewOrderDto;
import ru.aston.dto.OrderDto;
import ru.aston.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    @Mapping(target = "payment", ignore = true)
    OrderDto orderToOrderDto(Order order);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customerId", ignore = true)
    @Mapping(target = "executorId", ignore = true)
    @Mapping(target = "paymentId", ignore = true)
    @Mapping(target = "status", constant = "NEW")
    @Mapping(target = "date", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    Order newOrderDtoToOrder(NewOrderDto newOrderDto);
}