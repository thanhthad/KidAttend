package KidAttend.demo.service;

import KidAttend.demo.dto.request.classroom.*;
import KidAttend.demo.dto.response.classroom.ClassResponse;
import KidAttend.demo.dto.response.user.UserResponse;
import org.springframework.data.domain.*;

import java.util.List;

public interface ClassService {

    ClassResponse create(CreateClassRequest request);

    ClassResponse update(Long id, UpdateClassRequest request);

    void delete(Long id);

    ClassResponse getById(Long id);

    ClassResponse getByTeacherId(Long id);

    ClassResponse getByClassByMe();

    Page<ClassResponse> getAll(Pageable pageable);

    Page<ClassResponse> search(ClassSearchRequest request, Pageable pageable);

    List<UserResponse> getUnassignedTeachers();
}