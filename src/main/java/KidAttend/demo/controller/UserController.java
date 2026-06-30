package KidAttend.demo.controller;

import KidAttend.demo.common.response.ResponseData;
import KidAttend.demo.dto.request.user.BulkCreateUserRequest;
import KidAttend.demo.dto.request.user.UpdateUserInfo;
import KidAttend.demo.dto.request.user.UpdateUserPassword;
import KidAttend.demo.dto.request.user.UserCreateRequest;
import KidAttend.demo.dto.response.user.UserResponse;
import KidAttend.demo.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "User Management", description = "User APIs")
public class UserController {

    private final UserService userService;

    // ================= UPDATE USER INFO =================
    @PutMapping
    public ResponseEntity<?> updateUserInfo(
            @Valid @RequestBody UpdateUserInfo request
    ) {

        UserResponse response =
                userService.updateUserInfo(request);

        return ResponseData.success(
                response,
                "Cập nhật thông tin người dùng thành công",
                HttpStatus.OK
        );
    }

    // ================= GET CURRENT USER =================
    @GetMapping("/me")
    public ResponseEntity<?> getUserByMe() {

        UserResponse response = userService.findByMe();

        return ResponseData.success(
                response,
                "Lấy thông tin người dùng thành công",
                HttpStatus.OK
        );
    }

    // ================= CHANGE PASSWORD =================
    @PatchMapping("/password")
    public ResponseEntity<?> changePassword(
            @Valid @RequestBody UpdateUserPassword request
    ) {

        UserResponse response =
                userService.changePassword(request);

        return ResponseData.success(
                response,
                "Đổi mật khẩu thành công",
                HttpStatus.OK
        );
    }

    // ================= GET ALL USERS =================
    @GetMapping
    public ResponseEntity<?> getAllUsers(Pageable pageable) {

        Page<UserResponse> response = userService.findAll(pageable);

        return ResponseData.success(
                response,
                "Lấy danh sách người dùng thành công",
                HttpStatus.OK
        );
    }

    // ================= SEARCH BY FULL NAME =================
    @GetMapping("/search/fullname")
    public ResponseEntity<?> searchByFullName(
            @RequestParam String keyword,
            Pageable pageable
    ) {

        Page<UserResponse> response =
                userService.findByFullName(keyword, pageable);

        return ResponseData.success(
                response,
                "Tìm kiếm người dùng theo tên thành công",
                HttpStatus.OK
        );
    }

    // ================= SEARCH BY PHONE =================
    @GetMapping("/search/phone")
    public ResponseEntity<?> searchByPhone(
            @RequestParam String phone,
            Pageable pageable
    ) {

        Page<UserResponse> response =
                userService.findByPhone(phone, pageable);

        return ResponseData.success(
                response,
                "Tìm kiếm người dùng theo số điện thoại thành công",
                HttpStatus.OK
        );
    }

    // ================= GET BY ID =================
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {

        UserResponse response = userService.findById(id);

        return ResponseData.success(
                response,
                "Lấy thông tin người dùng thành công",
                HttpStatus.OK
        );
    }

    // ================= GET BY EMAIL =================
    @GetMapping("/by-email")
    public ResponseEntity<?> getUserByEmail(@RequestParam String email) {

        UserResponse response = userService.findByEmail(email);

        return ResponseData.success(
                response,
                "Lấy thông tin người dùng theo email thành công",
                HttpStatus.OK
        );
    }

    // ================= DELETE USER =================
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {

        userService.deleteUser(id);

        return ResponseData.success(
                null,
                "Xóa người dùng thành công",
                HttpStatus.OK
        );
    }

    // ================= CREATE USER =================
    @PostMapping
    public ResponseEntity<?> createUsers(
            @Valid @RequestBody UserCreateRequest request
    ) {

        userService.create(request);

        return ResponseData.success(
                null,
                "Tạo người dùng thành công",
                HttpStatus.CREATED
        );
    }

    // ================= BULK CREATE USERS =================
    @PostMapping("/bulk")
    public ResponseEntity<?> createUsers(
            @Valid @RequestBody BulkCreateUserRequest request
    ) {

        List<UserResponse> response =
                userService.createUsers(request);

        return ResponseData.success(
                response,
                "Tạo nhiều người dùng thành công",
                HttpStatus.CREATED
        );
    }
}