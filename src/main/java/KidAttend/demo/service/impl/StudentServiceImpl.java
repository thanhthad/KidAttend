package KidAttend.demo.service.impl;

import KidAttend.demo.dto.request.student.BulkCreateStudentRequest;
import KidAttend.demo.dto.request.student.BulkStudentRequest;
import KidAttend.demo.dto.request.student.CreateStudentRequest;
import KidAttend.demo.dto.request.student.UpdateStudentRequest;
import KidAttend.demo.dto.response.student.StudentResponse;
import KidAttend.demo.entity.ClassEntity;
import KidAttend.demo.entity.Student;
import KidAttend.demo.exception.classroom.ClassRoomNotFoundException;
import KidAttend.demo.exception.student.StudentAlreadyExistsException;
import KidAttend.demo.exception.student.StudentNotFoundException;
import KidAttend.demo.repository.ClassRepository;
import KidAttend.demo.repository.StudentRepository;
import KidAttend.demo.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final ClassRepository classRepository;

    // ================= CREATE =================
    @Override
    public StudentResponse create(CreateStudentRequest request) {

        validateDuplicateOnCreate(request);

        ClassEntity classEntity = getClassById(request.getClassId());

        Student student = new Student();
        student.setClassEntity(classEntity);
        student.setFullName(request.getFullName());
        student.setGender(request.getGender());
        student.setDateOfBirth(request.getDateOfBirth());
        student.setParentName(request.getParentName());

        // optional fields
        student.setParentPhone(request.getParentPhone());
        student.setParentEmail(request.getParentEmail());

        student.setAddress(request.getAddress());
        student.setStatus(request.getStatus());

        studentRepository.save(student);

        return mapToResponse(student);
    }

    // ================= UPDATE (PATCH STYLE) =================
    @Override
    public StudentResponse update(Long id, UpdateStudentRequest request) {

        Student student = getStudentById(id);

        if (request.getClassId() != null) {
            student.setClassEntity(getClassById(request.getClassId()));
        }

        if (isNotBlank(request.getFullName())) {
            student.setFullName(request.getFullName());
        }

        if (isNotBlank(request.getGender())) {
            student.setGender(request.getGender());
        }

        if (request.getDateOfBirth() != null) {
            student.setDateOfBirth(request.getDateOfBirth());
        }

        if (isNotBlank(request.getParentName())) {
            student.setParentName(request.getParentName());
        }

        if (isNotBlank(request.getParentPhone())) {
            validatePhoneDuplicate(request.getParentPhone(), id);
            student.setParentPhone(request.getParentPhone());
        }

        if (isNotBlank(request.getParentEmail())) {
            validateEmailDuplicate(request.getParentEmail(), id);
            student.setParentEmail(request.getParentEmail());
        }

        if (isNotBlank(request.getAddress())) {
            student.setAddress(request.getAddress());
        }

        if (isNotBlank(request.getStatus())) {
            student.setStatus(request.getStatus());
        }

        studentRepository.save(student);

        return mapToResponse(student);
    }

    // ================= DELETE =================
    @Override
    public void delete(Long id) {

        Student student = getStudentById(id);

        studentRepository.delete(student);
    }

    // ================= GET BY ID =================
    @Override
    public StudentResponse getById(Long id) {

        return mapToResponse(getStudentById(id));
    }

    // ================= SEARCH =================
    @Override
    public Page<StudentResponse> search(Long classId, String name, String address, Pageable pageable) {

        Page<Student> page;

        if (classId != null && name != null) {
            page = studentRepository.findByClassEntity_IdAndFullNameContainingIgnoreCase(
                    classId, name, pageable);

        } else if (classId != null) {
            page = studentRepository.findByClassEntity_Id(classId, pageable);

        } else if (name != null) {
            page = studentRepository.findByFullNameContainingIgnoreCase(name, pageable);

        } else if (address != null) {
            page = studentRepository.findByAddressContainingIgnoreCase(address, pageable);

        } else {
            page = studentRepository.findAll(pageable);
        }

        return page.map(this::mapToResponse);
    }

    // ================= HELPERS =================

    private Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Student not found: " + id));
    }

    private ClassEntity getClassById(Long classId) {
        return classRepository.findById(classId)
                .orElseThrow(() -> new ClassRoomNotFoundException("Class not found: " + classId));
    }

    private void validateDuplicateOnCreate(CreateStudentRequest request) {

        if (request.getParentPhone() != null &&
                studentRepository.existsByParentPhone(request.getParentPhone())) {

            throw new StudentAlreadyExistsException(
                    "Parent phone already exists: " + request.getParentPhone()
            );
        }

        if (request.getParentEmail() != null &&
                studentRepository.existsByParentEmail(request.getParentEmail())) {

            throw new StudentAlreadyExistsException(
                    "Parent email already exists: " + request.getParentEmail()
            );
        }
    }

    private void validatePhoneDuplicate(String phone, Long id) {
        studentRepository.findByParentPhone(phone)
                .ifPresent(s -> {
                    if (!s.getId().equals(id)) {
                        throw new StudentAlreadyExistsException("Parent phone already exists");
                    }
                });
    }

    private void validateEmailDuplicate(String email, Long id) {
        studentRepository.findByParentEmail(email)
                .ifPresent(s -> {
                    if (!s.getId().equals(id)) {
                        throw new StudentAlreadyExistsException("Parent email already exists");
                    }
                });
    }

    private boolean isNotBlank(String value) {
        return value != null && !value.isBlank();
    }

    // ================= MAPPER =================
    private StudentResponse mapToResponse(Student student) {

        return StudentResponse.builder()
                .id(student.getId())
                .classId(student.getClassEntity().getId())
                .className(student.getClassEntity().getName())
                .fullName(student.getFullName())
                .gender(student.getGender())
                .dateOfBirth(student.getDateOfBirth())
                .parentName(student.getParentName())
                .parentPhone(student.getParentPhone())
                .parentEmail(student.getParentEmail())
                .address(student.getAddress())
                .status(student.getStatus())
                .build();
    }

    @Override
    public List<StudentResponse> bulkCreate(BulkCreateStudentRequest request) {

        ClassEntity classEntity = classRepository.findById(request.getClassId())
                .orElseThrow(() -> new ClassRoomNotFoundException("Class not found: " + request.getClassId()));

        List<Student> students = new ArrayList<>();

        for (BulkStudentRequest req : request.getStudents()) {

            // check duplicate phone/email (DB)
            if (studentRepository.existsByParentPhone(req.getParentPhone())) {
                throw new StudentAlreadyExistsException(
                        "Parent phone already exists: " + req.getParentPhone()
                );
            }

            if (studentRepository.existsByParentEmail(req.getParentEmail())) {
                throw new StudentAlreadyExistsException(
                        "Parent email already exists: " + req.getParentEmail()
                );
            }

            Student student = new Student();
            student.setClassEntity(classEntity);
            student.setFullName(req.getFullName());
            student.setGender(req.getGender());
            student.setDateOfBirth(req.getDateOfBirth());
            student.setParentName(req.getParentName());
            student.setParentPhone(req.getParentPhone());
            student.setParentEmail(req.getParentEmail());
            student.setAddress(req.getAddress());
            student.setStatus(req.getStatus());

            students.add(student);
        }

        // 3. save batch
        List<Student> saved = studentRepository.saveAll(students);

        // 4. map response
        return saved.stream()
                .map(this::mapToResponse)
                .toList();
    }
}