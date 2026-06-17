package KidAttend.demo.controller;

import KidAttend.demo.common.response.ResponseData;
import KidAttend.demo.dto.request.user.UpdateUserInfo;
import KidAttend.demo.dto.request.user.UpdateUserPassword;
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

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "User Management", description = "User APIs")
public class UserController {

    private final UserService userService;

    // ================= GET ALL USERS =================
    @GetMapping
    public ResponseEntity<?> getAllUsers(Pageable pageable) {

        Page<UserResponse> response = userService.findAll(pageable);

        return ResponseData.success(
                response,
                "Get all users successfully",
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
                "Search users by full name successfully",
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
                "Search users by phone successfully",
                HttpStatus.OK
        );
    }

    // ================= GET BY ID =================
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {

        UserResponse response = userService.findById(id);

        return ResponseData.success(
                response,
                "Get user successfully",
                HttpStatus.OK
        );
    }

    // ================= GET BY EMAIL =================
    @GetMapping("/email")
    public ResponseEntity<?> getUserByEmail(@RequestParam String email) {

        UserResponse response = userService.findByEmail(email);

        return ResponseData.success(
                response,
                "Get user by email successfully",
                HttpStatus.OK
        );
    }

    // ================= UPDATE USER INFO =================
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUserInfo(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserInfo request
    ) {

        UserResponse response =
                userService.updateUserInfo(id, request);

        return ResponseData.success(
                response,
                "Update user info successfully",
                HttpStatus.OK
        );
    }

    // ================= CHANGE PASSWORD =================
    @PatchMapping("/{id}/password")
    public ResponseEntity<?> changePassword(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserPassword request
    ) {

        UserResponse response =
                userService.changePassword(id, request);

        return ResponseData.success(
                response,
                "Change password successfully",
                HttpStatus.OK
        );
    }

    // ================= DELETE USER =================
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {

        userService.deleteUser(id);

        return ResponseData.success(
                null,
                "Delete user successfully",
                HttpStatus.OK
        );
    }
}