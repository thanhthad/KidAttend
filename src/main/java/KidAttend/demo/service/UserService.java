package KidAttend.demo.service;

import KidAttend.demo.dto.request.user.BulkCreateUserRequest;
import KidAttend.demo.dto.request.user.UpdateUserInfo;
import KidAttend.demo.dto.request.user.UpdateUserPassword;
import KidAttend.demo.dto.request.user.UserCreateRequest;
import KidAttend.demo.dto.response.user.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    Page<UserResponse> findByFullName (String userName, Pageable pageable);

    Page<UserResponse> findByPhone(String phoneNumber , Pageable pageable);

    Page<UserResponse> findAll(Pageable pageable);

    UserResponse findById(Long id);

    UserResponse findByMe();

    UserResponse findByEmail(String email);

    UserResponse changePassword(UpdateUserPassword updateUserPassword);

    UserResponse updateUserInfo(UpdateUserInfo updateUserInfo);

    void deleteUser(Long id);

    List<UserResponse> createUsers(BulkCreateUserRequest request);

    void create(UserCreateRequest request);

}
