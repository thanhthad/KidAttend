package KidAttend.demo.service;

import KidAttend.demo.dto.request.student.BulkCreateStudentRequest;
import KidAttend.demo.dto.request.student.CreateStudentRequest;
import KidAttend.demo.dto.request.student.UpdateStudentRequest;
import KidAttend.demo.dto.response.student.StudentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StudentService {

    StudentResponse create(CreateStudentRequest request);

    StudentResponse update(Long id, UpdateStudentRequest request);

    void delete(Long id);

    StudentResponse getById(Long id);

    Page<StudentResponse> search(Long classId, String name, String address, Pageable pageable);

    List<StudentResponse> bulkCreate(BulkCreateStudentRequest request);
}