package KidAttend.demo.service;

import KidAttend.demo.dto.request.student.BulkCreateStudentRequest;
import KidAttend.demo.dto.request.student.StudentCreateAndUpdate;
import KidAttend.demo.dto.response.student.StudentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
public interface StudentService {

    Page<StudentResponse> findAll(Pageable pageable);

    Page<StudentResponse> search(
            Long classId,
            String name,
            String address,
            String parentEmail,
            String parentPhone,
            Pageable pageable
    );

    StudentResponse findById(Long id);

    StudentResponse create(Long classId, StudentCreateAndUpdate request);

    void changeClass(Long studentId, Long newClassId);

    List<StudentResponse> createBulk(Long classId, BulkCreateStudentRequest request);

    StudentResponse update(Long id, StudentCreateAndUpdate request);

    void delete(Long id);
}