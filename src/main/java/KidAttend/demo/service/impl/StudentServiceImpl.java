package KidAttend.demo.service.impl;

import KidAttend.demo.dto.request.student.BulkCreateStudentRequest;
import KidAttend.demo.dto.request.student.StudentCreateAndUpdate;
import KidAttend.demo.dto.response.student.StudentResponse;
import KidAttend.demo.exception.student.StudentAlreadyExistsException;
import KidAttend.demo.repository.StudentRepository;
import KidAttend.demo.service.StudentService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    @Override
    public Page<StudentResponse> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public Page<StudentResponse> search(Long classId, String name, String address, String parentEmail, String parentPhone, Pageable pageable) {
        return null;
    }

    @Override
    public StudentResponse findById(Long id) {
        return null;
    }

    @Override
    public StudentResponse create(Long classId, StudentCreateAndUpdate request) {

        if(request.getParentEmail() != null && studentRepository.existsByParentEmail(request.getParentEmail())){
            throw new StudentAlreadyExistsException("Student already exists with email: " + request.getParentEmail());
        }if(request.getParentPhone() != null && studentRepository.existsByParentPhone(request.getParentPhone())){
            throw new StudentAlreadyExistsException("Student already exists with phone number:" + request.getParentPhone());
        }
        return null;
    }

    @Override
    public void changeClass(Long studentId, Long newClassId) {

    }

    @Override
    public List<StudentResponse> createBulk(Long classId, BulkCreateStudentRequest request) {
        return List.of();
    }

    @Override
    public StudentResponse update(Long id, StudentCreateAndUpdate request) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
