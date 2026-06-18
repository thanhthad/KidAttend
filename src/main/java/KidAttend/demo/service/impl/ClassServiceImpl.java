package KidAttend.demo.service.impl;

import KidAttend.demo.dto.request.classroom.*;
import KidAttend.demo.dto.response.classroom.ClassResponse;
import KidAttend.demo.dto.response.user.TeacherResponse;
import KidAttend.demo.entity.*;
import KidAttend.demo.exception.classroom.ClassNotFoundException;
import KidAttend.demo.repository.ClassRepository;
import KidAttend.demo.service.ClassService;
import KidAttend.demo.service.UserServiceDomain;
import KidAttend.demo.specification.ClassSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClassServiceImpl implements ClassService {

    private final ClassRepository classRepository;
    private final UserServiceDomain userServiceDomain;

    @Override
    public ClassResponse create(CreateClassRequest request) {

        User teacher = null;
        if (request.getTeacherId() != null) {
            teacher = userServiceDomain.getByUserId(request.getTeacherId());
        }

        ClassEntity entity = ClassEntity.builder()
                .name(request.getName())
                .age(request.getAge())
                .capacity(request.getCapacity())
                .description(request.getDescription())
                .status(ClassStatus.ACTIVE)
                .teacher(teacher)
                .build();

        classRepository.save(entity);

        return mapToResponse(entity);
    }

    @Override
    public ClassResponse update(Long id, UpdateClassRequest request) {

        ClassEntity entity = classRepository.findById(id)
                .orElseThrow(() -> new ClassNotFoundException("Class not found: " + id));

        entity.setName(request.getName());
        entity.setAge(request.getAge());
        entity.setCapacity(request.getCapacity());
        entity.setDescription(request.getDescription());
        entity.setStatus(request.getStatus());

        if (request.getTeacherId() != null) {
            User teacher = userServiceDomain.getByUserId(request.getTeacherId());

            if (!"TEACHER".equalsIgnoreCase(teacher.getRole())) {
                throw new IllegalArgumentException("User is not a teacher");
            }

            entity.setTeacher(teacher);
        }

        classRepository.save(entity);

        return mapToResponse(entity);
    }

    @Override
    public void delete(Long id) {
        ClassEntity entity = classRepository.findById(id)
                .orElseThrow(() -> new ClassNotFoundException("Class not found: " + id));

        classRepository.delete(entity);
    }

    @Override
    public ClassResponse getById(Long id) {
        ClassEntity entity = classRepository.findById(id)
                .orElseThrow(() -> new ClassNotFoundException("Class not found: " + id));

        return mapToResponse(entity);
    }

    @Override
    public Page<ClassResponse> getAll(Pageable pageable) {
        return classRepository.findAll(pageable)
                .map(this::mapToResponse);
    }

    @Override
    public Page<ClassResponse> search(ClassSearchRequest request, Pageable pageable) {

        Specification<ClassEntity> spec = Specification
                .where(ClassSpecification.nameContains(request.getName()))
                .and(ClassSpecification.hasAge(request.getAge()))
                .and(ClassSpecification.hasStatus(request.getStatus()))
                .and(ClassSpecification.hasTeacher(request.getTeacherId()));

        return classRepository.findAll(spec, pageable)
                .map(this::mapToResponse);
    }

    // ================= mapper =================

    private ClassResponse mapToResponse(ClassEntity entity) {

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
                .currentStudents(0)
                .build();
    }
}