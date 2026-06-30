package KidAttend.demo.service.impl;

import KidAttend.demo.dto.request.classroom.*;
import KidAttend.demo.dto.response.classroom.ClassResponse;
import KidAttend.demo.dto.response.user.TeacherResponse;
import KidAttend.demo.dto.response.user.UserResponse;
import KidAttend.demo.entity.*;
import KidAttend.demo.exception.classroom.ClassRoomAlreadyExistsException;
import KidAttend.demo.exception.classroom.ClassRoomNotFoundException;
import KidAttend.demo.exception.student.InvalidAgeException;
import KidAttend.demo.exception.student.StudentAlreadyExistsException;
import KidAttend.demo.exception.user.UserNotFoundException;
import KidAttend.demo.repository.UserRepository;
import KidAttend.demo.repository.projection.ClassProjection;
import KidAttend.demo.repository.ClassRepository;
import KidAttend.demo.repository.StudentRepository;
import KidAttend.demo.security.userdetails.SecurityUtils;
import KidAttend.demo.service.ClassService;
import KidAttend.demo.service.UserServiceDomain;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClassServiceImpl implements ClassService {

    private final ClassRepository classRepository;
    private final UserServiceDomain userServiceDomain;
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;

    // ================= CREATE =================
    @Override
    public ClassResponse create(CreateClassRequest request) {

        User teacher = null;

        if (request.getTeacherId() != null) {

            teacher = userServiceDomain.getByUserId(request.getTeacherId());

            if (classRepository.existsByTeacherId(request.getTeacherId())) {
                throw new ClassRoomAlreadyExistsException("Giáo viên đã được phân vào lớp khác");
            }
        }

        ClassEntity entity = ClassEntity.builder()
                .name(request.getName())
                .age(request.getAge())
                .capacity(request.getCapacity())
                .description(request.getDescription())
                .status(ClassStatus.ACTIVE)
                .teacher(teacher)
                .build();

        return mapEntityToResponse(classRepository.save(entity));
    }

    // ================= UPDATE =================
    @Override
    public ClassResponse update(Long id, UpdateClassRequest request) {

        ClassEntity entity = classRepository.findById(id)
                .orElseThrow(() ->
                        new ClassRoomNotFoundException("Không tìm thấy lớp với id: " + id));

        // ================= NAME =================
        if (request.getName() != null) {
            if (request.getName().isBlank()) {
                throw new IllegalArgumentException("Tên lớp không được để trống");
            }
            entity.setName(request.getName());
        }

        // ================= AGE =================
        if (request.getAge() != null) {

            if (request.getAge() <= 0) {
                throw new IllegalArgumentException("Độ tuổi phải lớn hơn 0");
            }

            if (request.getAge() > 6) {
                throw new IllegalArgumentException("Độ tuổi không được lớn hơn 6");
            }

            entity.setAge(request.getAge());
        }

        // ================= CAPACITY =================
        if (request.getCapacity() != null) {

            if (request.getCapacity() <= 0) {
                throw new IllegalArgumentException("Sức chứa phải lớn hơn 0");
            }
            if (request.getCapacity() > 50) {
                throw new IllegalArgumentException("Sức chứa không được vượt quá 50");
            }

            long activeCount = studentRepository
                    .countByClassEntityIdAndStatus(id, "ACTIVE");

            if (request.getCapacity() < activeCount) {
                throw new IllegalArgumentException(
                        "Sức chứa không được nhỏ hơn số học sinh hiện tại: " + activeCount
                );
            }

            entity.setCapacity(request.getCapacity());
        }

        // ================= DESCRIPTION =================
        if (request.getDescription() != null) {
            if (request.getDescription().isBlank()) {
                throw new IllegalArgumentException("Mô tả không được để trống");
            }
            entity.setDescription(request.getDescription());
        }

        // ================= STATUS =================
        if (request.getStatus() != null) {
            entity.setStatus(request.getStatus());
        }

        // ================= REMOVE TEACHER =================
        if (Boolean.TRUE.equals(request.getRemoveTeacher())) {
            entity.setTeacher(null);
        }

        // ================= UPDATE TEACHER =================
        else if (request.getTeacherId() != null) {

            User teacher = userServiceDomain.getByUserId(request.getTeacherId());

            boolean existing = classRepository.existsByTeacherId(request.getTeacherId());

            if (existing) {
                throw new IllegalArgumentException("Giáo viên đã được phân vào lớp khác");
            }

            entity.setTeacher(teacher);
        }

        return mapEntityToResponse(classRepository.save(entity));
    }

    // ================= DELETE =================
    @Override
    public void delete(Long id) {

        ClassEntity entity = classRepository.findById(id)
                .orElseThrow(() ->
                        new ClassRoomNotFoundException("Không tìm thấy lớp với id: " + id));

        if (studentRepository.existsByClassEntity_Id(id)) {
            throw new StudentAlreadyExistsException("Không thể xóa lớp vì vẫn còn học sinh đang thuộc lớp này");
        }

        classRepository.delete(entity);
    }

    // ================= GET BY ID =================
    @Override
    public ClassResponse getById(Long id) {

        ClassEntity entity = classRepository.findById(id)
                .orElseThrow(() ->
                        new ClassRoomNotFoundException("Không tìm thấy lớp với id: " + id));

        return mapEntityToResponse(entity);
    }

    @Override
    public ClassResponse getByTeacherId(Long id) {
        ClassEntity entity = classRepository.findByTeacher_Id(id)
                .orElseThrow(() ->
                        new UserNotFoundException("Không tìm thấy giáo viên với id: " + id));
        return mapEntityToResponse(entity);
    }

    @Override
    public ClassResponse getByClassByMe() {
        Long id = SecurityUtils.getCurrentUserId();
        ClassEntity entity = classRepository.findByTeacher_Id(id)
                .orElseThrow(() ->
                        new UserNotFoundException("Không tìm thấy giáo viên với id: " + id));
        return mapEntityToResponse(entity);
    }

    // ================= GET ALL =================
    @Override
    public Page<ClassResponse> getAll(Pageable pageable) {
        return classRepository.findAllWithTeacherAndCount(pageable)
                .map(this::mapProjectionToResponse);
    }

    // ================= SEARCH =================
    @Override
    public Page<ClassResponse> search(ClassSearchRequest request, Pageable pageable) {

        if (request.getName() == null) {
            request.setName("");
        }

        return classRepository.searchClasses(
                request.getName(),
                request.getAge(),
                request.getStatus(),
                request.getTeacherId(),
                pageable
        ).map(this::mapProjectionToResponse);
    }

    private UserResponse map(User u) {
        return UserResponse.builder()
                .id(u.getId())
                .fullName(u.getFullName())
                .email(u.getEmail())
                .phone(u.getPhone())
                .build();
    }

    @Override
    public List<UserResponse> getUnassignedTeachers() {

        List<User> users = userRepository.findUnassignedTeachers();

        return users.stream()
                .map(this::map)
                .toList();
    }

    // ================= ENTITY MAPPER =================
    private ClassResponse mapEntityToResponse(ClassEntity entity) {

        Long studentCount = studentRepository.countByClassEntityIdAndStatus(entity.getId(), "ACTIVE");

        TeacherResponse teacher = null;

        if (entity.getTeacher() != null) {
            teacher = TeacherResponse.builder()
                    .id(entity.getTeacher().getId())
                    .fullName(entity.getTeacher().getFullName())
                    .email(entity.getTeacher().getEmail())
                    .phone(entity.getTeacher().getPhone())
                    .build();
        }

        return ClassResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .age(entity.getAge())
                .capacity(entity.getCapacity())
                .description(entity.getDescription())
                .status(entity.getStatus())
                .teacher(teacher)
                .currentStudents(studentCount)
                .build();
    }

    // ================= PROJECTION MAPPER =================
    private ClassResponse mapProjectionToResponse(ClassProjection p) {

        TeacherResponse teacher = null;

        if (p.getTeacherId() != null) {
            teacher = TeacherResponse.builder()
                    .id(p.getTeacherId())
                    .fullName(p.getTeacherName())
                    .email(p.getTeacherEmail())
                    .phone(p.getTeacherPhone())
                    .build();
        }

        return ClassResponse.builder()
                .id(p.getId())
                .name(p.getName())
                .age(p.getAge())
                .capacity(p.getCapacity())
                .description(p.getDescription())
                .status(ClassStatus.valueOf(p.getStatus()))
                .teacher(teacher)
                .currentStudents(p.getCurrentStudents())
                .build();
    }

    private void validateAgeForKindergarten(LocalDate dob) {

        int age = java.time.Period.between(dob, java.time.LocalDate.now()).getYears();

        if (age < 1 || age > 6) {
            throw new InvalidAgeException(
                    "Độ tuổi học sinh phải nằm trong khoảng 1 đến 6 tuổi"
            );
        }
    }
}