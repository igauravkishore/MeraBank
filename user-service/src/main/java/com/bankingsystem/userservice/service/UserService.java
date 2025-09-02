package com.bankingsystem.userservice.service;

import com.bankingsystem.userservice.dto.UserRequest;
import com.bankingsystem.userservice.dto.UserResponse;
import com.bankingsystem.userservice.exceptions.UserAlreadyExistsException;
import com.bankingsystem.userservice.model.User;
import com.bankingsystem.userservice.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;


@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserResponse createUser(UserRequest userRequest) throws UserAlreadyExistsException {
        if (userRepository.existsByEmail(userRequest.getEmail()) ||
                userRepository.existsByPhoneNumber(userRequest.getPhoneNumber())) {
            throw new UserAlreadyExistsException("Email or Phone number already exists");
        }
        User user = new User();
        user.setEmail(userRequest.getEmail());
        user.setFullName(userRequest.getFullName());
        user.setUsername(userRequest.getUsername());
        user.setPhoneNumber(userRequest.getPhoneNumber());
        user.setKeycloakId(userRequest.getKeycloakId());
        user.setPassword(bCryptPasswordEncoder.encode(userRequest.getPassword()));
        user.setCreatedAt(LocalDate.now());

        User savedUser = userRepository.save(user);

        UserResponse userResponse = new UserResponse();
        userResponse.setId(savedUser.getId());
        userResponse.setEmail(savedUser.getEmail());
        userResponse.setUsername(savedUser.getUsername());
        userResponse.setPassword(savedUser.getPassword());
        userResponse.setKeycloakId(savedUser.getKeycloakId());
        userResponse.setFullName(savedUser.getFullName());
        userResponse.setCreatedAt(LocalDate.now());
        userResponse.setUpdatedAt(LocalDate.now());
        userResponse.setPhoneNumber(savedUser.getPhoneNumber());
        return userResponse;

    }

    public Optional<User> getUserByUserId(Long userId) {
        return userRepository.findById(userId);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Boolean validateUser(String userId){
        return userRepository.existsByKeycloakId(userId);
    }

    public void updateUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setUpdatedAt(LocalDate.now());
        userRepository.save(user);
    }

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}
