package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
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
        return userMapping.mapToUserDto(
                userRepository.save(
                        userMapping.mapToUser(userDto)));
    }

    @Override
    public UserDto getUserById(long id) {
        checkUserExistence(id);
        return userMapping.mapToUserDto(
                userRepository.getReferenceById(id));
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapping::mapToUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto updateUser(long id, UserDto userDto) {
        checkEmailExistence(userDto);
        checkUserExistence(id);
        User user = userRepository.getReferenceById(id);
        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail());
        }
        userRepository.save(user);
        return userMapping.mapToUserDto(user);
    }

    @Override
    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }

    @Override
    public void checkUserExistence(long id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("User not found");
        }
        ;
    }

    private void checkEmailExistence(UserDto userDto) {
        if (userRepository.findAll()
                .stream()
                .map(User::getEmail)
                .collect(Collectors.toList())
                .contains(userDto.getEmail())) {
            log.error("ValidationException: {}", "User email already used");
            throw new ValidationException("User email already used");
        }
    }
}
