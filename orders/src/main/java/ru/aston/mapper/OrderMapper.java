package ru.aston.mapper;

import ru.aston.dto.NewOrderDto;
import ru.aston.dto.OrderDto;
import ru.aston.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * Mapper interface defines methods for converting
 * order entities to order DTOs and new order DTOs to order entities.
 */
@Mapper
public interface OrderMapper {

    /**
     * Instance of the OrderMapper interface.
     */
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    /**
     * Maps an order entity to an order DTO.
     * @param order The order entity to map.
     * @return The corresponding order DTO.
     */
    @Mapping(target = "payment", ignore = true)
    OrderDto orderToOrderDto(Order order);

    /**
     * Maps a new order DTO to an order entity.
     * @param newOrderDto The new order DTO to map.
     * @return The corresponding order entity.
     */
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