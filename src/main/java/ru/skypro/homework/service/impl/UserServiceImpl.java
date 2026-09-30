package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.skypro.homework.api.dto.NewPassword;
import ru.skypro.homework.api.dto.UpdateUser;
import ru.skypro.homework.api.dto.User;
import ru.skypro.homework.db.model.UserModel;
import ru.skypro.homework.db.repository.UserRepository;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.service.UserService;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public User getCurrentUser(String username) {
        return userMapper.toDto(getCurrentUserModel(username));
    }

    @Override
    @Transactional(readOnly = true)
    public UserModel getCurrentUserModel(String username) {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND));
    }

    @Override
    @Transactional
    public UpdateUser updateCurrentUser(String username, UpdateUser updateUser) {
        UserModel user = getCurrentUserModel(username);
        userMapper.updateModel(updateUser, user);
        return userMapper.toUpdateUser(userRepository.save(user));
    }

    @Override
    @Transactional
    public void setPassword(String username, NewPassword newPassword) {
        UserModel user = getCurrentUserModel(username);
        if (!passwordEncoder.matches(newPassword.getCurrentPassword(), user.getPassword())) {
            throw new ResponseStatusException(BAD_REQUEST);
        }
        user.setPassword(passwordEncoder.encode(newPassword.getNewPassword()));
        userRepository.save(user);
    }
}
