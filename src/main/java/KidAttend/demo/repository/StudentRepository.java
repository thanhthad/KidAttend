package KidAttend.demo.repository;

import KidAttend.demo.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByParentPhone(String parentPhone);

    Optional<Student> findByParentEmail(String parentEmail);

    Page<Student> findByFullNameContainingIgnoreCase(String fullName, Pageable pageable);

    Page<Student> findByAddressContainingIgnoreCase(String address, Pageable pageable);

    Page<Student> findByClassEntity_Id(Long classId, Pageable pageable);

    Page<Student> findByClassEntity_IdAndFullNameContainingIgnoreCase(
            Long classId,
            String fullName,
            Pageable pageable
    );

    boolean existsByParentPhone(String parentPhone);

    boolean existsByParentEmail(String parentEmail);
}