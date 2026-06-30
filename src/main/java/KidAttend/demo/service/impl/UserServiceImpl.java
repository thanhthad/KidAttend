package KidAttend.demo.service.impl;

import KidAttend.demo.dto.request.user.BulkCreateUserRequest;
import KidAttend.demo.dto.request.user.UpdateUserInfo;
import KidAttend.demo.dto.request.user.UpdateUserPassword;
import KidAttend.demo.dto.request.user.UserCreateRequest;
import KidAttend.demo.dto.response.user.UserResponse;
import KidAttend.demo.entity.User;
import KidAttend.demo.exception.user.EmailAlreadyExistsException;
import KidAttend.demo.exception.user.PhoneAlreadyExistsException;
import KidAttend.demo.exception.user.UserAlreadyExistsException;
import KidAttend.demo.exception.user.UserNotFoundException;
import KidAttend.demo.repository.AttendanceRepository;
import KidAttend.demo.repository.ClassRepository;
import KidAttend.demo.repository.UserRepository;
import KidAttend.demo.security.userdetails.SecurityUtils;
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
    private final AttendanceRepository attendanceRepository;
    private final ClassRepository classRepository;

    @Override
    public Page<UserResponse> findAll(Pageable pageable) {
        return userRepository.findAllUsers(pageable);
    }

    @Override
    public Page<UserResponse> findByFullName(String userName, Pageable pageable) {
        return userRepository.findByFullName(userName, pageable);
    }

    @Override
    public Page<UserResponse> findByPhone(String phoneNumber, Pageable pageable) {
        return userRepository.findByPhone(phoneNumber, pageable);
    }

    @Override
    public UserResponse findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Không tìm thấy người dùng"));

        return mapToResponse(user);
    }

    @Override
    public UserResponse findByMe() {
        Long id = SecurityUtils.getCurrentUserId();
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Không tìm thấy người dùng"));

        return mapToResponse(user);
    }

    @Override
    public UserResponse changePassword(UpdateUserPassword req) {
        Long userId = SecurityUtils.getCurrentUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Không tìm thấy người dùng"));

        if (!passwordEncoder.matches(req.getOldPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Mật khẩu cũ không đúng");
        }

        user.setPasswordHash(passwordEncoder.encode(req.getNewPassword()));

        return mapToResponse(userRepository.save(user));
    }

    @Override
    public UserResponse updateUserInfo(UpdateUserInfo req) {
        Long userId = SecurityUtils.getCurrentUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Không tìm thấy người dùng"));

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

    @Override
    public UserResponse findByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Không tìm thấy người dùng"));

        return mapToResponse(user);
    }

    @Override
    public void deleteUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Không tìm thấy người dùng"));

        boolean hasAttendance = attendanceRepository.existsByCreatedById(id);
        if (hasAttendance) {
            throw new UserAlreadyExistsException(
                    "Người dùng đã tạo dữ liệu điểm danh, không thể xoá"
            );
        }

        boolean isTeacher = classRepository.existsByTeacherId(id);
        if (isTeacher) {
            throw new UserAlreadyExistsException(
                    "Người dùng đang là giáo viên của lớp, không thể xoá"
            );
        }

        userRepository.delete(user);
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
                throw new UserAlreadyExistsException("Trùng email trong request: " + dto.getEmail());
            }

            if (dto.getPhone() != null && !phones.add(dto.getPhone())) {
                throw new PhoneAlreadyExistsException("Trùng số điện thoại trong request: " + dto.getPhone());
            }

            if (userRepository.existsByEmail(dto.getEmail())) {
                throw new EmailAlreadyExistsException("Email đã tồn tại: " + dto.getEmail());
            }

            if (dto.getPhone() != null && userRepository.existsByPhone(dto.getPhone())) {
                throw new PhoneAlreadyExistsException("Số điện thoại đã tồn tại: " + dto.getPhone());
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

    @Override
    @Transactional
    public void create(UserCreateRequest dto) {

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new EmailAlreadyExistsException("Email đã tồn tại: " + dto.getEmail());
        }

        if (dto.getPhone() != null && userRepository.existsByPhone(dto.getPhone())) {
            throw new PhoneAlreadyExistsException("Số điện thoại đã tồn tại: " + dto.getPhone());
        }

        User user = User.builder()
                .fullName(dto.getFullName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .passwordHash(passwordEncoder.encode(dto.getPassword()))
                .role("USER")
                .status("ACTIVE")
                .build();

        userRepository.save(user);
    }
}