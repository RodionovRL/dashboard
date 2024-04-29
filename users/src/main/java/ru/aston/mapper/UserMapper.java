package ru.aston.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.aston.dto.NewUserDto;
import ru.aston.dto.UserDto;
import ru.aston.model.User;

/**
 * Mapper interface defines methods for converting
 * user entities to user DTOs and new user DTOs to user entities.
 */
@Mapper
public interface UserMapper {

    /**
     * Instance of the UserMapper interface.
     */
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    /**
     * Maps a user entity to a user DTO.
     * @param user The user entity to map.
     * @return The corresponding user DTO.
     */
    UserDto userToUserDto(User user);

    /**
     * Maps a new user DTO to a user entity.
     * @param newUserDto The new user DTO to map.
     * @return The corresponding user entity.
     */
    @Mapping(target = "id", ignore = true)
    User newUserDtoToUser(NewUserDto newUserDto);
}