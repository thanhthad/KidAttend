package KidAttend.demo.service;

import KidAttend.demo.dto.request.user.UpdateUserInfo;
import KidAttend.demo.dto.request.user.UpdateUserPassword;
import KidAttend.demo.dto.response.user.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    Page<UserResponse> findByFullName (String userName, Pageable pageable);

    Page<UserResponse> findByPhone(String phoneNumber , Pageable pageable);

    Page<UserResponse> findAll(Pageable pageable);

    UserResponse findById(Long id);

    UserResponse findByEmail(String email);

    UserResponse changePassword(Long userId ,UpdateUserPassword updateUserPassword);

    UserResponse updateUserInfo(Long userId ,UpdateUserInfo updateUserInfo);

    void deleteUser(Long id);
}
