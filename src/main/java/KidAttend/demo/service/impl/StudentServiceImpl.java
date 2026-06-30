package KidAttend.demo.service.impl;

import KidAttend.demo.dto.request.student.BulkCreateStudentRequest;
import KidAttend.demo.dto.request.student.BulkStudentRequest;
import KidAttend.demo.dto.request.student.CreateStudentRequest;
import KidAttend.demo.dto.request.student.UpdateStudentRequest;
import KidAttend.demo.dto.response.student.StudentResponse;
import KidAttend.demo.entity.ClassEntity;
import KidAttend.demo.entity.Student;
import KidAttend.demo.exception.classroom.ClassRoomNotFoundException;
import KidAttend.demo.exception.student.InvalidAgeException;
import KidAttend.demo.exception.student.StudentAlreadyExistsException;
import KidAttend.demo.exception.student.StudentNotFoundException;
import KidAttend.demo.repository.ClassRepository;
import KidAttend.demo.repository.StudentRepository;
import KidAttend.demo.security.userdetails.SecurityUtils;
import KidAttend.demo.service.StudentService;
import KidAttend.demo.specification.StudentSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

        if (request.getParentPhone() != null &&
                studentRepository.existsByParentPhone(request.getParentPhone())) {

            throw new StudentAlreadyExistsException(
                    "Số điện thoại phụ huynh đã tồn tại: " + request.getParentPhone()
            );
        }

        if (request.getParentEmail() != null &&
                studentRepository.existsByParentEmail(request.getParentEmail())) {

            throw new StudentAlreadyExistsException(
                    "Email phụ huynh đã tồn tại: " + request.getParentEmail()
            );
        }

        ClassEntity classEntity = getClassById(request.getClassId());

        long count = studentRepository.countByClassEntityIdAndStatus(classEntity.getId(), "ACTIVE");

        if (count >= classEntity.getCapacity()) {
            throw new IllegalArgumentException(
                    "Lớp đã đầy với id: " + classEntity.getId()
            );
        }

        validateAgeForKindergarten(request.getDateOfBirth());

        Student student = new Student();
        student.setClassEntity(classEntity);
        student.setFullName(request.getFullName());
        student.setGender(request.getGender());
        student.setDateOfBirth(request.getDateOfBirth());
        student.setParentName(request.getParentName());

        student.setParentPhone(request.getParentPhone());
        student.setParentEmail(request.getParentEmail());

        student.setAddress(request.getAddress());
        student.setStatus(request.getStatus());

        studentRepository.save(student);

        return mapToResponse(student);
    }

    // ================= UPDATE =================
    @Override
    public StudentResponse update(Long id, UpdateStudentRequest request) {

        Student student = getStudentById(id);

        if (request.getClassId() != null) {

            ClassEntity newClass = getClassById(request.getClassId());

            long count = studentRepository
                    .countByClassEntityIdAndStatus(newClass.getId(), "ACTIVE");

            if (student.getClassEntity() == null ||
                    !student.getClassEntity().getId().equals(newClass.getId())) {

                if (count >= newClass.getCapacity()) {
                    throw new IllegalArgumentException(
                            "Lớp đích đã đầy: " + newClass.getId()
                    );
                }
            }

            student.setClassEntity(newClass);
        }

        if (isNotBlank(request.getFullName())) {
            student.setFullName(request.getFullName());
        }

        if (isNotBlank(request.getGender())) {
            student.setGender(request.getGender());
        }

        if (request.getDateOfBirth() != null) {
            validateAgeForKindergarten(request.getDateOfBirth());
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

    @Override
    public Page<StudentResponse> getAll(Pageable pageable) {
        return studentRepository.findAll(pageable)
                .map(this::convertToResponse);
    }

    private StudentResponse convertToResponse(Student student) {
        return StudentResponse.builder()
                .id(student.getId())
                .classId(student.getClassEntity() != null
                        ? student.getClassEntity().getId()
                        : null)
                .className(student.getClassEntity() != null
                        ? student.getClassEntity().getName()
                        : null)
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
    public List<StudentResponse> getAllByClassId(Long classId) {

        ClassEntity classEntity = getClassById(classId);

        List<Student> students =
                studentRepository.findAllByClassEntity_IdOrderByFullNameAsc(classEntity.getId());

        return students.stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<StudentResponse> getAllByMe() {

        Long userId = SecurityUtils.getCurrentUserId();

        Long classId = classRepository.findByTeacher_Id(userId)
                .orElseThrow(() ->
                        new ClassRoomNotFoundException("Giáo viên chưa được phân lớp")
                ).getId();

        ClassEntity classEntity = getClassById(classId);

        List<Student> students =
                studentRepository.findAllByClassEntity_IdOrderByFullNameAsc(classEntity.getId());

        return students.stream()
                .map(this::mapToResponse)
                .toList();
    }

    // ================= SEARCH =================
    @Override
    public Page<StudentResponse> search(Long classId, String name, String address, Pageable pageable) {

        Specification<Student> spec =
                StudentSpecification.filter(classId, name, address);

        return studentRepository.findAll(spec, pageable)
                .map(this::mapToResponse);
    }

    // ================= HELPERS =================
    private void validateAgeForKindergarten(LocalDate dob) {

        int age = java.time.Period.between(dob, java.time.LocalDate.now()).getYears();

        if (age < 1 || age > 6) {
            throw new InvalidAgeException(
                    "Độ tuổi học sinh phải từ 1 đến 6 tuổi"
            );
        }
    }

    private Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() ->
                        new StudentNotFoundException("Không tìm thấy học sinh: " + id));
    }

    private ClassEntity getClassById(Long classId) {
        return classRepository.findById(classId)
                .orElseThrow(() ->
                        new ClassRoomNotFoundException("Không tìm thấy lớp: " + classId));
    }

    private void validatePhoneDuplicate(String phone, Long id) {
        studentRepository.findByParentPhone(phone)
                .ifPresent(s -> {
                    if (!s.getId().equals(id)) {
                        throw new StudentAlreadyExistsException("Số điện thoại phụ huynh đã tồn tại");
                    }
                });
    }

    private void validateEmailDuplicate(String email, Long id) {
        studentRepository.findByParentEmail(email)
                .ifPresent(s -> {
                    if (!s.getId().equals(id)) {
                        throw new StudentAlreadyExistsException("Email phụ huynh đã tồn tại");
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
                .orElseThrow(() ->
                        new ClassRoomNotFoundException("Không tìm thấy lớp: " + request.getClassId()));

        List<Student> students = new ArrayList<>();

        for (BulkStudentRequest req : request.getStudents()) {

            if (studentRepository.existsByParentPhone(req.getParentPhone())) {
                throw new StudentAlreadyExistsException(
                        "Số điện thoại phụ huynh đã tồn tại: " + req.getParentPhone()
                );
            }

            if (studentRepository.existsByParentEmail(req.getParentEmail())) {
                throw new StudentAlreadyExistsException(
                        "Email phụ huynh đã tồn tại: " + req.getParentEmail()
                );
            }

            validateAgeForKindergarten(req.getDateOfBirth());

            long current =
                    studentRepository.countByClassEntityIdAndStatus(classEntity.getId(), "ACTIVE");
            long incoming = request.getStudents().size();

            if (current + incoming > classEntity.getCapacity()) {
                throw new IllegalArgumentException("Lớp đã đầy");
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

        List<Student> saved = studentRepository.saveAll(students);

        return saved.stream()
                .map(this::mapToResponse)
                .toList();
    }
}