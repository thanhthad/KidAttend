package KidAttend.demo.service;

import KidAttend.demo.dto.request.classroom.*;
import KidAttend.demo.dto.response.classroom.ClassResponse;
import org.springframework.data.domain.*;

public interface ClassService {

    ClassResponse create(CreateClassRequest request);

    ClassResponse update(Long id, UpdateClassRequest request);

    void delete(Long id);

    ClassResponse getById(Long id);

    ClassResponse getByTeacherId(Long id);

    Page<ClassResponse> getAll(Pageable pageable);

    Page<ClassResponse> search(ClassSearchRequest request, Pageable pageable);
}