package ru.aston.service;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.aston.dto.NewUserDto;
import ru.aston.dto.UserDto;
import ru.aston.mapper.UserMapper;
import ru.aston.model.User;
import ru.aston.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of UserService interface.
 */
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    /**
     * Logger for the {@link UserServiceImpl} class.
     */
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    UserRepository userRepository;

    @Override
    public UserDto postUser(NewUserDto newUserDto) {
        User user = UserMapper.INSTANCE.newUserDtoToUser(newUserDto);
        User createdUser = userRepository.save(user);
        log.debug("Created new user: {}", createdUser);
        return UserMapper.INSTANCE.userToUserDto(createdUser);
    }

    @Override
    public UserDto getUserById(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if(user.isPresent()) {
            log.debug("Retrieved user with ID: {} - ", userId, user.get());
            return UserMapper.INSTANCE.userToUserDto(user.get());
        } else {
            log.error("User with ID: {} not found", userId);
            throw new RuntimeException("User with id = " + userId + " not found");
        }
    }

    @Override
    public List<UserDto> getUserListByIds(List<Long> userIds) {
        List<User> usersList = userRepository.findByIdIn(userIds);
        List<UserDto> userDtoList = usersList.stream()
                .map(UserMapper.INSTANCE::userToUserDto)
                .collect(Collectors.toList());
        log.debug("Retrieved users list by specified IDs: {}", userDtoList);
        return userDtoList;
    }
}