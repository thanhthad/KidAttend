package KidAttend.demo.service.impl;

import KidAttend.demo.dto.request.user.UpdateUserInfo;
import KidAttend.demo.dto.request.user.UpdateUserPassword;
import KidAttend.demo.dto.response.user.UserResponse;
import KidAttend.demo.entity.User;
import KidAttend.demo.exception.user.UserNotFoundException;
import KidAttend.demo.repository.UserRepository;
import KidAttend.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
                .fullName(user.getFullName())
                .phone(user.getPhone())
                .email(user.getEmail())
                .build();
    }
}