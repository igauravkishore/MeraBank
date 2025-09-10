package com.bankingsystem.userservice.service;

import com.bankingsystem.userservice.dto.JwtResponse;
import com.bankingsystem.userservice.dto.LoginRequest;
import com.bankingsystem.userservice.dto.RegisterRequest;
import com.bankingsystem.userservice.dto.UserResponse;
import com.bankingsystem.userservice.model.Role;
import com.bankingsystem.userservice.model.User;
import com.bankingsystem.userservice.repository.UserRepository;
import com.bankingsystem.userservice.util.JwtTokenUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtTokenUtil jwtTokenUtil;

    public UserResponse registerUser(RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new RuntimeException("Username is already taken!");
        }
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("Email is already in use!");
        }

        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setUsername(registerRequest.getUsername());
        user.setPhoneNumber(registerRequest.getPhoneNumber());
        user.setPassword(bCryptPasswordEncoder.encode(registerRequest.getPassword()));
        user.setRoles(Set.of(Role.USER));
        user.setCreatedAt(LocalDate.now());

        User savedUser = userRepository.save(user);
        return mapToUserResponse(savedUser);
    }

    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!bCryptPasswordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        Set<String> roles = user.getRoles().stream()
                .map(Enum::name)
                .collect(Collectors.toSet());

        String token = jwtTokenUtil.generateToken(user.getUsername(), roles);

        return new JwtResponse(token, user.getUsername(), user.getEmail(), roles);
    }

    public Optional<User> getUserByUserId(Long userId) {
        return userRepository.findById(userId);
    }

    public UserResponse getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return mapToUserResponse(user);
    }

    private UserResponse mapToUserResponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setEmail(user.getEmail());
        userResponse.setUsername(user.getUsername());
        userResponse.setPassword(user.getPassword());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setCreatedAt(LocalDate.now());
        userResponse.setUpdatedAt(LocalDate.now());
        userResponse.setPhoneNumber(user.getPhoneNumber());
        return userResponse;

    }


    public void updateUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setUpdatedAt(LocalDate.now());
        userRepository.save(user);
    }

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
