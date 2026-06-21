package KidAttend.demo.repository;

import KidAttend.demo.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long>,
        JpaSpecificationExecutor<Student> {

    Optional<Student> findByParentPhone(String parentPhone);
    Optional<Student> findByParentEmail(String parentEmail);

    List<Student> findAllByClassEntity_Id(Long classId);

    long countByClassEntity_Id(Long classId);

    boolean existsByParentPhone(String parentPhone);
    boolean existsByParentEmail(String parentEmail);
}