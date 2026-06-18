package KidAttend.demo.repository;

import KidAttend.demo.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Page<Student> findByClassEntity_Id(Long classId, Pageable pageable);

    @Query("""
        SELECT s FROM Student s
        WHERE LOWER(s.fullName) LIKE LOWER(CONCAT('%', :name, '%'))
    """)
    Page<Student> searchByName(@Param("name") String name, Pageable pageable);

    @Query("""
        SELECT s FROM Student s
        WHERE LOWER(s.address) LIKE LOWER(CONCAT('%', :address, '%'))
    """)
    Page<Student> searchByAddress(@Param("address") String address, Pageable pageable);

    @Query("""
        SELECT s FROM Student s
        WHERE s.classEntity.id = :classId
        AND LOWER(s.fullName) LIKE LOWER(CONCAT('%', :name, '%'))
    """)
    Page<Student> searchByNameAndClass(
            @Param("classId") Long classId,
            @Param("name") String name,
            Pageable pageable
    );

    @Query("""
        SELECT s FROM Student s
        WHERE s.classEntity.id = :classId
        AND LOWER(s.address) LIKE LOWER(CONCAT('%', :address, '%'))
    """)
    Page<Student> searchByAddressAndClass(
            @Param("classId") Long classId,
            @Param("address") String address,
            Pageable pageable
    );

    Optional<Student> findByParentEmail(String email);

    Optional<Student> findByParentPhone(String phone);

    boolean existsByParentEmail(String email);

    boolean existsByParentPhone(String phone);

    @Query("SELECT s FROM Student s")
    Page<Student> findAllStudents(Pageable pageable);
}