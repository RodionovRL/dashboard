package ru.aston.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.aston.dto.NewUserDto;
import ru.aston.dto.UserDto;
import ru.aston.model.User;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto userToUserDto(User user);

    @Mapping(target = "id", ignore = true)
    User newUserDtoToUser(NewUserDto newUserDto);
}
