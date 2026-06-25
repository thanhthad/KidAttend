package KidAttend.demo.service.impl;

import KidAttend.demo.dto.request.classroom.*;
import KidAttend.demo.dto.response.classroom.ClassResponse;
import KidAttend.demo.dto.response.user.TeacherResponse;
import KidAttend.demo.entity.*;
import KidAttend.demo.exception.classroom.ClassRoomAlreadyExistsException;
import KidAttend.demo.exception.classroom.ClassRoomNotFoundException;
import KidAttend.demo.exception.student.InvalidAgeException;
import KidAttend.demo.exception.user.UserNotFoundException;
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

@Service
@RequiredArgsConstructor
public class ClassServiceImpl implements ClassService {

    private final ClassRepository classRepository;
    private final UserServiceDomain userServiceDomain;
    private final StudentRepository studentRepository;

    // ================= CREATE =================

    @Override
    public ClassResponse create(CreateClassRequest request) {

        User teacher = null;

        if (request.getTeacherId() != null) {

            teacher = userServiceDomain.getByUserId(request.getTeacherId());

            if (classRepository.existsByTeacherId(request.getTeacherId())) {
                throw new ClassRoomAlreadyExistsException("Teacher already assigned to another class");
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
                        new ClassRoomNotFoundException("Class not found: " + id));

        // ================= NAME =================
        if (request.getName() != null) {
            if (request.getName().isBlank()) {
                throw new IllegalArgumentException("Class name cannot be blank");
            }
            entity.setName(request.getName());
        }

        // ================= AGE =================
        if (request.getAge() != null) {

            if (request.getAge() <= 0) {
                throw new IllegalArgumentException("Age must be > 0");
            }

            if(request.getAge() > 6 ){
                throw  new IllegalArgumentException("Age must be > 6");
            }

            entity.setAge(request.getAge());
        }

        // ================= CAPACITY =================
        if (request.getCapacity() != null) {

            if (request.getCapacity() <= 0) {
                throw new IllegalArgumentException("Capacity must be > 0");
            }
            if (request.getCapacity() > 50) {
                throw new IllegalArgumentException("Capacity must be > 50");
            }

            long activeCount = studentRepository
                    .countByClassEntityIdAndStatus(id, "ACTIVE");

            if (request.getCapacity() < activeCount) {
                throw new IllegalArgumentException(
                        "Capacity cannot be lower than current ACTIVE students: " + activeCount
                );
            }

            entity.setCapacity(request.getCapacity());
        }

        // ================= DESCRIPTION =================
        if (request.getDescription() != null) {
            if (request.getDescription().isBlank()) {
                throw new IllegalArgumentException("Description cannot be blank");
            }
            entity.setDescription(request.getDescription());
        }

        // ================= STATUS =================
        if (request.getStatus() != null) {
            entity.setStatus(request.getStatus());
        }

        // ================= TEACHER =================
        if (request.getTeacherId() != null) {

            User teacher = userServiceDomain.getByUserId(request.getTeacherId());

            boolean existing = classRepository
                    .existsByTeacherId(request.getTeacherId());

            if(existing){
                throw new IllegalArgumentException("Teacher already sign with another class");
            }

            entity.setTeacher(teacher);
        }

        return mapEntityToResponse(classRepository.save(entity));
    }

    // ================= DELETE =================

    @Override
    public void delete(Long id) {

        ClassEntity entity = classRepository.findById(id)
                .orElseThrow(() -> new ClassRoomNotFoundException("Class not found: " + id));

        classRepository.delete(entity);
    }

    // ================= GET BY ID =================

    @Override
    public ClassResponse getById(Long id) {

        ClassEntity entity = classRepository.findById(id)
                .orElseThrow(() -> new ClassRoomNotFoundException("Class not found: " + id));

        return mapEntityToResponse(entity);
    }

    @Override
    public ClassResponse getByTeacherId(Long id) {
        ClassEntity entity = classRepository.findByTeacher_Id(id)
                .orElseThrow(() -> new UserNotFoundException("Teacher not found with id:" + id));
        return mapEntityToResponse(entity);
    }

    @Override
    public ClassResponse getByClassByMe() {
        Long id = SecurityUtils.getCurrentUserId();
        ClassEntity entity = classRepository.findByTeacher_Id(id)
                .orElseThrow(() -> new UserNotFoundException("Teacher not found with id:" + id));
        return mapEntityToResponse(entity);
    }

    // ================= GET ALL =================

    @Override
    public Page<ClassResponse> getAll(Pageable pageable) {

        return classRepository.findAllWithTeacherAndCount(pageable)
                .map(this::mapProjectionToResponse);
    }

    // ================= SEARCH (PROJECTION) =================

    @Override
    public Page<ClassResponse> search(ClassSearchRequest request, Pageable pageable) {

        if(request.getName() == null){
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

    // ================= ENTITY MAPPER =================

    private ClassResponse mapEntityToResponse(ClassEntity entity) {

        Long studentCount = studentRepository.countByClassEntityIdAndStatus(entity.getId(),"ACTIVE");

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
                    "Student age must be between 1 and 6 years old"
            );
        }
    }
}