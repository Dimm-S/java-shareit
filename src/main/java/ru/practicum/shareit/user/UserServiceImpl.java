package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapping userMapping;

    @Override
    public UserDto saveUser(UserDto userDto) {
        checkEmailExistence(userDto);
        return userMapping.mapToUserDto(
                userRepository.saveUser(
                        userMapping.mapToUser(userDto)));
    }

    @Override
    public UserDto getUserById(long id) {
        checkUserExistence(id);
        return userMapping.mapToUserDto(
                userRepository.getUserById(id));
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.getAllUsers().stream()
                .map(userMapping::mapToUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto updateUser(long id, UserDto userDto) {
        checkEmailExistence(userDto);
        checkUserExistence(id);
        return userMapping.mapToUserDto(
                userRepository.updateUser(
                        id, userMapping.mapToUser(userDto)));
    }

    @Override
    public void deleteUser(long id) {
        userRepository.deleteUser(id);
    }

    public void checkUserExistence(long id) {
        userRepository.checkUserExistence(id);
    }

    private void checkEmailExistence(UserDto userDto) {
        if (userRepository.getAllUsers()
                .stream()
                .map(User::getEmail)
                .collect(Collectors.toList())
                .contains(userDto.getEmail())) {
            log.error("ValidationException: {}", "User email already used");
            throw new ValidationException("User email already used");
        }
    }
}
