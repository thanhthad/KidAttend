package KidAttend.demo.service.impl;

import KidAttend.demo.dto.request.user.BulkCreateUserRequest;
import KidAttend.demo.dto.request.user.UpdateUserInfo;
import KidAttend.demo.dto.request.user.UpdateUserPassword;
import KidAttend.demo.dto.response.user.UserResponse;
import KidAttend.demo.entity.User;
import KidAttend.demo.exception.user.EmailAlreadyExistsException;
import KidAttend.demo.exception.user.PhoneAlreadyExistsException;
import KidAttend.demo.exception.user.UserAlreadyExistsException;
import KidAttend.demo.exception.user.UserNotFoundException;
import KidAttend.demo.repository.UserRepository;
import KidAttend.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Page<UserResponse> findAll(Pageable pageable) {
        return userRepository.findAllUsers(pageable);
    }

    @Override
    public Page<UserResponse> findByFullName(String userName, Pageable pageable) {
        return userRepository.findByFullName(userName,pageable);
    }

    @Override
    public Page<UserResponse> findByPhone(String phoneNumber, Pageable pageable) {
        return userRepository.findByPhone(phoneNumber,pageable);
    }

    @Override
    public UserResponse findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return mapToResponse(user);
    }

    // =========================
    @Override
    public UserResponse findByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return mapToResponse(user);
    }

    @Override
    public UserResponse changePassword(Long userId,UpdateUserPassword req) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!passwordEncoder.matches(req.getOldPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Old password is incorrect");
        }

        user.setPasswordHash(passwordEncoder.encode(req.getNewPassword()));

        return mapToResponse(userRepository.save(user));
    }

    @Override
    public UserResponse updateUserInfo(Long userId,UpdateUserInfo req) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (req.getFullName() != null) {
            user.setFullName(req.getFullName());
        }

        if (req.getPhone() != null) {
            user.setPhone(req.getPhone());
        }

        if (req.getEmail() != null) {
            user.setEmail(req.getEmail());
        }

        userRepository.save(user);
        return mapToResponse(user);
    }

    // =========================
    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User not found");
        }

        userRepository.deleteById(id);
    }

    private UserResponse mapToResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .phone(user.getPhone())
                .email(user.getEmail())
                .build();
    }

    @Override
    @Transactional
    public List<UserResponse> createUsers(BulkCreateUserRequest request) {

        Set<String> emails = new HashSet<>();
        Set<String> phones = new HashSet<>();

        List<User> users = new ArrayList<>();

        for (var dto : request.getUsers()) {

            if (!emails.add(dto.getEmail())) {
                throw new UserAlreadyExistsException("Duplicate email in request: " + dto.getEmail());
            }

            if (dto.getPhone() != null && !phones.add(dto.getPhone())) {
                throw new PhoneAlreadyExistsException("Duplicate phone in request: " + dto.getPhone());
            }

            if (userRepository.existsByEmail(dto.getEmail())) {
                throw new EmailAlreadyExistsException("Email already exists: " + dto.getEmail());
            }

            if (dto.getPhone() != null && userRepository.existsByPhone(dto.getPhone())) {
                throw new PhoneAlreadyExistsException("Phone already exists: " + dto.getPhone());
            }

            users.add(User.builder()
                    .fullName(dto.getFullName())
                    .email(dto.getEmail())
                    .phone(dto.getPhone())
                    .passwordHash(passwordEncoder.encode(dto.getPassword()))
                    .role("USER")
                    .status("ACTIVE")
                    .build());
        }

        return userRepository.saveAll(users)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }
}