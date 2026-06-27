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

    List<Student> findAllByClassEntity_IdOrderByFullNameAsc(Long classId);

    long countByClassEntityIdAndStatus(Long classId, String status);

    boolean existsByParentPhone(String parentPhone);
    boolean existsByParentEmail(String parentEmail);
    boolean existsByClassEntity_Id(Long classId);

}